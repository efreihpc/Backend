package backend.model.SPHPC;

import static org.jocl.CL.CL_CONTEXT_PLATFORM;
import static org.jocl.CL.CL_DEVICE_TYPE_ALL;
import static org.jocl.CL.CL_MEM_COPY_HOST_PTR;
import static org.jocl.CL.CL_MEM_READ_ONLY;
import static org.jocl.CL.CL_MEM_READ_WRITE;
import static org.jocl.CL.CL_TRUE;
import static org.jocl.CL.clBuildProgram;
import static org.jocl.CL.clCreateBuffer;
import static org.jocl.CL.clCreateCommandQueue;
import static org.jocl.CL.clCreateContext;
import static org.jocl.CL.clCreateKernel;
import static org.jocl.CL.clCreateProgramWithSource;
import static org.jocl.CL.clEnqueueNDRangeKernel;
import static org.jocl.CL.clEnqueueReadBuffer;
import static org.jocl.CL.clGetDeviceIDs;
import static org.jocl.CL.clGetPlatformIDs;
import static org.jocl.CL.clReleaseCommandQueue;
import static org.jocl.CL.clReleaseContext;
import static org.jocl.CL.clReleaseKernel;
import static org.jocl.CL.clReleaseMemObject;
import static org.jocl.CL.clReleaseProgram;
import static org.jocl.CL.clSetKernelArg;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.persistence.Transient;

import org.jocl.CL;
import org.jocl.Pointer;
import org.jocl.Sizeof;
import org.jocl.cl_command_queue;
import org.jocl.cl_context;
import org.jocl.cl_context_properties;
import org.jocl.cl_device_id;
import org.jocl.cl_kernel;
import org.jocl.cl_mem;
import org.jocl.cl_platform_id;
import org.jocl.cl_program;

import backend.model.job.JobPlugin;
import backend.model.result.JsonResult;
import backend.system.MongoPersistenceUnit;

public class MonteCarloJob extends JobPlugin<JsonResult> {
	
	@Transient
	MongoPersistenceUnit m_mongoPersistence;
	
	@Transient
	private cl_context m_context;
	
	@Transient
	private cl_device_id m_device;
	
	@Transient
	private cl_mem m_memObjects[];
	
    //input- and output data 
    float m_srcArrayA[];
    float m_srcArrayB[];
    float m_dstArray[];
	
	@Transient
	private Pointer m_resultArray;
	
	@Transient
	private int m_n;

	@Override
	protected void execute() {
		initializePlatform();
		
		cl_kernel kernel = createKernel("PrototypeKernel.cl");
		prepareKernel(kernel);
		runKernel(kernel);
		release(kernel);
		
		validate();
	}
	
	public void setMongoPersistence(MongoPersistenceUnit mongoPersistence)
	{
		m_mongoPersistence = mongoPersistence;
		result(new JsonResult(m_mongoPersistence));
	}
	
	private void initializePlatform()
	{
        // The platform, device type and device number
        // that will be used
        final int platformIndex = 0;
        final long deviceType = CL_DEVICE_TYPE_ALL;
        final int deviceIndex = 0;

        // Enable exceptions and subsequently omit error checks in this sample
        CL.setExceptionsEnabled(true);

        // Obtain the number of platforms
        int numPlatformsArray[] = new int[1];
        clGetPlatformIDs(0, null, numPlatformsArray);
        int numPlatforms = numPlatformsArray[0];

        // Obtain a platform ID
        cl_platform_id platforms[] = new cl_platform_id[numPlatforms];
        clGetPlatformIDs(platforms.length, platforms, null);
        cl_platform_id platform = platforms[platformIndex];

        // Initialize the context properties
        cl_context_properties contextProperties = new cl_context_properties();
        contextProperties.addProperty(CL_CONTEXT_PLATFORM, platform);
        
        // Obtain the number of devices for the platform
        int numDevicesArray[] = new int[1];
        clGetDeviceIDs(platform, deviceType, 0, null, numDevicesArray);
        int numDevices = numDevicesArray[0];
        
        // Obtain a device ID 
        cl_device_id devices[] = new cl_device_id[numDevices];
        clGetDeviceIDs(platform, deviceType, numDevices, devices, null);
        cl_device_id device = devices[deviceIndex];

        // Create a context for the selected device
        m_context = clCreateContext(
            contextProperties, 1, new cl_device_id[]{device}, 
            null, null, null);
	}
	
	cl_kernel createKernel(String sourcePath)
	{
		String source = readFile(sourcePath);

        // Create the program from the source code
        cl_program program = clCreateProgramWithSource(m_context,
            1, new String[]{source}, null, null);
        
        // Build the program
        clBuildProgram(program, 0, null, null, null, null);
        
        // Create the kernel
        return clCreateKernel(program, "MCKernel", null);
	}
	
	private cl_kernel prepareKernel(cl_kernel kernel)
	{
        // Create input- and output data 
        m_srcArrayA = new float[m_n];
        m_srcArrayB = new float[m_n];
        m_dstArray = new float[m_n];
        for (int i = 0; i < m_n; i++)
        {
            m_srcArrayA[i] = i;
            m_srcArrayB[i] = i;
        }
        Pointer srcA = Pointer.to(m_srcArrayA);
        Pointer srcB = Pointer.to(m_srcArrayB);
        m_resultArray = Pointer.to(m_dstArray);
		
        m_memObjects = new cl_mem[3];
        m_memObjects[0] = clCreateBuffer(m_context, 
            CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
            Sizeof.cl_float * m_n, srcA, null);
        m_memObjects[1] = clCreateBuffer(m_context, 
            CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
            Sizeof.cl_float * m_n, srcB, null);
        m_memObjects[2] = clCreateBuffer(m_context, 
            CL_MEM_READ_WRITE, 
            Sizeof.cl_float * m_n, null, null);
        
        // Set the arguments for the kernel
        clSetKernelArg(kernel, 0, 
            Sizeof.cl_mem, Pointer.to(m_memObjects[0]));
        clSetKernelArg(kernel, 1, 
            Sizeof.cl_mem, Pointer.to(m_memObjects[1]));
        clSetKernelArg(kernel, 2, 
            Sizeof.cl_mem, Pointer.to(m_memObjects[2]));
		
		return kernel;
	}
	
	private void runKernel(cl_kernel kernel)
	{
        // Create a command-queue for the selected device
        cl_command_queue commandQueue = 
            clCreateCommandQueue(m_context, m_device, 0, null);
        
        // Set the work-item dimensions
        long global_work_size[] = new long[]{m_n};
        long local_work_size[] = new long[]{1};
        
        // Execute the kernel
        clEnqueueNDRangeKernel(commandQueue, kernel, 1, null,
            global_work_size, local_work_size, 0, null, null);
        
        // Read the output data
        clEnqueueReadBuffer(commandQueue, m_memObjects[2], CL_TRUE, 0,
            m_n * Sizeof.cl_float, m_resultArray, 0, null, null);
	}
	
	private void release(cl_kernel kernel)
	{
        // Release kernel, program, and memory objects
        clReleaseMemObject(m_memObjects[0]);
        clReleaseMemObject(m_memObjects[1]);
        clReleaseMemObject(m_memObjects[2]);
        clReleaseKernel(kernel);
//        clReleaseProgram(program);
//        clReleaseCommandQueue(queue);
        clReleaseContext(m_context);
	}
	
	private void validate()
	{
	       // Verify the result
        boolean passed = true;
        final float epsilon = 1e-7f;
        for (int i = 0; i < m_n; i++)
        {
            float x = m_dstArray[i];
            float y = m_srcArrayA[i] * m_srcArrayB[i];
            boolean epsilonEqual = Math.abs(x - y) <= epsilon * Math.abs(x);
            if (!epsilonEqual)
            {
                passed = false;
                break;
            }
        }

        if (m_n <= 10)
        {
        	result().insert("{	'state':'" + (passed?"PASSED":"FAILED") + "'," + 
        					"	'result':" + java.util.Arrays.toString(m_dstArray));
        }
	}
	
    private String readFile(String fileName)
    {
        try
        {
            BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(fileName)));
            StringBuffer sb = new StringBuffer();
            String line = null;
            while (true)
            {
                line = br.readLine();
                if (line == null)
                {
                    break;
                }
                sb.append(line).append("\n");
            }
            return sb.toString();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }

}
