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
import static org.jocl.CL.clReleaseContext;
import static org.jocl.CL.clReleaseKernel;
import static org.jocl.CL.clReleaseMemObject;
import static org.jocl.CL.clSetKernelArg;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
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

import ro.fortsoft.pf4j.Extension;
import backend.model.job.JobPlugin;
import backend.model.result.JsonResult;
import backend.system.MongoPersistenceUnit;

@Extension
@Entity
@Inheritance
public class BlackScholesJob extends JobPlugin<JsonResult> {
	
	@Transient
	MongoPersistenceUnit m_mongoPersistence;
	
	@Transient
	private cl_context m_context;
	
	@Transient
	private cl_device_id m_device;
	
	@Transient
	private cl_mem m_memObjects[];
	
	@Transient
	private float m_riskFree[] = new float[]{(float) 0.05};
	@Transient
	private float m_sigma[] = new float[]{(float) 0.2}; // volatility
	
    //input- and output data 
	@Transient
    float m_stockPrice[];
	@Transient
    float m_strikePrice[];
	@Transient
    int m_randomSeedX[];
	@Transient
    int m_randomSeedY[];
	@Transient
    float m_time[];
	@Transient
    float m_optionCall[];
	@Transient
    float m_optionPut[];
	
	@Transient
	private Pointer m_resultCall;
	@Transient
	private Pointer m_resultPut;
	
	@Transient
//	private int m_globalSize = 65536;
	private int m_globalSize = 10;

	@Override
	public void execute() {
		initializePlatform();
		
		cl_kernel kernel = createKernel("3rd_party/HPC-Plugin-0.1.0/classes/kernel/BlackScholesKernel.cl");
		kernel = prepareKernel(kernel);
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
        m_device = devices[deviceIndex];

        // Create a context for the selected device
        m_context = clCreateContext(
            contextProperties, 1, new cl_device_id[]{m_device}, 
            null, null, null);
	}
	
	cl_kernel createKernel(String sourcePath)
	{
		String source = readFile(sourcePath);

        // Create the program from the source code
        cl_program program = clCreateProgramWithSource(m_context,
            1, new String[]{source}, null, null);
        
        // Build the program
        clBuildProgram(program, 0, null, "-cl-denorms-are-zero -cl-fast-relaxed-math -cl-single-precision-constant -DNSAMP=262144", null, null);
        
        // Create the kernel
        return clCreateKernel(program, "BlackScholesKernel", null);
	}
	
	private cl_kernel prepareKernel(cl_kernel kernel)
	{
        // Create input- and output data 
        m_stockPrice = new float[m_globalSize];
        m_strikePrice = new float[m_globalSize];
        m_time = new float[m_globalSize];
        m_randomSeedX = new int[m_globalSize];
        m_randomSeedY = new int[m_globalSize];
        m_optionPut = new float[m_globalSize];
        m_optionCall = new float[m_globalSize];
        
        float min1 = 10;
        float max1 = 50;
        float min2 = (float) 0.2;
        float max2 = 2;
        
        for (int i = 0; i < m_globalSize; i++)
        {
            m_stockPrice[i] = m_stockPrice[i]*(max1-min1)+min1;
            m_strikePrice[i] = m_strikePrice[i]*(max1-min1)+min1;
            m_time[i] = m_time[i]*(max1-min1)+min1;
            m_optionPut[i] = 0;
            m_optionCall[i] = 0;
            m_randomSeedX[i] = i%(i + 1);
            m_randomSeedY[i] = i%(i + 2);
        }
        Pointer srcA = Pointer.to(m_stockPrice);
        Pointer srcB = Pointer.to(m_strikePrice);
        Pointer srcC = Pointer.to(m_time);
        Pointer srcD = Pointer.to(m_randomSeedX);
        Pointer srcE = Pointer.to(m_riskFree);
        Pointer srcF = Pointer.to(m_sigma);
        Pointer srcG = Pointer.to(m_randomSeedY);
        m_resultCall = Pointer.to(m_optionCall);
        m_resultPut = Pointer.to(m_optionPut);
		
        m_memObjects = new cl_mem[9];
        m_memObjects[0] = clCreateBuffer(m_context, 
            CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
            Sizeof.cl_float * m_globalSize, srcA, null);
        m_memObjects[1] = clCreateBuffer(m_context, 
            CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
            Sizeof.cl_float * m_globalSize, srcB, null);
        m_memObjects[2] = clCreateBuffer(m_context, 
            CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
            Sizeof.cl_float * m_globalSize, srcC, null);
        m_memObjects[3] = clCreateBuffer(m_context, 
            CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
            Sizeof.cl_int * m_globalSize, srcD, null);
        m_memObjects[4] = clCreateBuffer(m_context, 
            CL_MEM_READ_WRITE, 
            Sizeof.cl_float * m_globalSize, null, null);
        m_memObjects[5] = clCreateBuffer(m_context, 
            CL_MEM_READ_WRITE, 
            Sizeof.cl_float * m_globalSize, null, null);
        m_memObjects[6] = clCreateBuffer(m_context, 
            CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
            Sizeof.cl_float, srcE, null);
        m_memObjects[7] = clCreateBuffer(m_context, 
            CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
            Sizeof.cl_float, srcF, null);
        m_memObjects[8] = clCreateBuffer(m_context, 
            CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
            Sizeof.cl_int * m_globalSize, srcG, null);
        
        // Set the arguments for the kernel
        clSetKernelArg(kernel, 0, 
            Sizeof.cl_mem, Pointer.to(m_memObjects[4]));
        clSetKernelArg(kernel, 1, 
            Sizeof.cl_mem, Pointer.to(m_memObjects[5]));
        clSetKernelArg(kernel, 2, 
            Sizeof.cl_mem, Pointer.to(m_memObjects[3]));
        clSetKernelArg(kernel, 3, 
            Sizeof.cl_mem, Pointer.to(m_memObjects[8]));
        clSetKernelArg(kernel, 4, 
            Sizeof.cl_float, Pointer.to(m_riskFree));
        clSetKernelArg(kernel, 5, 
            Sizeof.cl_float, Pointer.to(m_sigma));
        clSetKernelArg(kernel, 6, 
            Sizeof.cl_mem, Pointer.to(m_memObjects[0]));
        clSetKernelArg(kernel, 7, 
            Sizeof.cl_mem, Pointer.to(m_memObjects[1]));
        clSetKernelArg(kernel, 8, 
            Sizeof.cl_mem, Pointer.to(m_memObjects[2]));
		
		return kernel;
	}
	
	private void runKernel(cl_kernel kernel)
	{
        // Create a command-queue for the selected device
        cl_command_queue commandQueue = 
            clCreateCommandQueue(m_context, m_device, 0, null);
        
        // Set the work-item dimensions
        long global_work_size[] = new long[]{m_globalSize};
        long local_work_size[] = new long[]{1};
        
        System.out.println("MonteCarloJob: Running Kernel");
        // Execute the kernel
        clEnqueueNDRangeKernel(commandQueue, kernel, 1, null,
            global_work_size, local_work_size, 0, null, null);
        
        System.out.println("MonteCarloJob: Fetching Data From Kernel");
        // Read the output data
        clEnqueueReadBuffer(commandQueue, m_memObjects[4], CL_TRUE, 0,
        		m_globalSize * Sizeof.cl_float, m_resultCall, 0, null, null);
        // Read the output data
        clEnqueueReadBuffer(commandQueue, m_memObjects[5], CL_TRUE, 0,
        		m_globalSize * Sizeof.cl_float, m_resultPut, 0, null, null);
	}
	
	private void release(cl_kernel kernel)
	{
        // Release kernel, program, and memory objects
        clReleaseMemObject(m_memObjects[0]);
        clReleaseMemObject(m_memObjects[1]);
        clReleaseMemObject(m_memObjects[2]);
        clReleaseMemObject(m_memObjects[3]);
        clReleaseMemObject(m_memObjects[4]);
        clReleaseMemObject(m_memObjects[5]);
        clReleaseMemObject(m_memObjects[6]);
        clReleaseMemObject(m_memObjects[7]);
        clReleaseMemObject(m_memObjects[8]);
        clReleaseKernel(kernel);
//        clReleaseProgram(program);
//        clReleaseCommandQueue(queue);
        clReleaseContext(m_context);
	}
	
	private void validate()
	{
        boolean passed = true;
        
        System.out.println("MonteCarloJob: Persisting Results!!");
        System.out.println("{	'state':'" + (passed?"PASSED":"FAILED") + "'," + 
				"	'result_put':" + java.util.Arrays.toString(m_optionPut) + 
				"	'result_call':" + java.util.Arrays.toString(m_optionCall) + 
				"}");

    	result().insert("{	'state':'" + (passed?"PASSED":"FAILED") + "'," + 
    					"	'result_put':" + java.util.Arrays.toString(m_optionPut) + 
    					"	'result_call':" + java.util.Arrays.toString(m_optionCall) + 
    					"}");
	}
	
    private String readFile(String fileName)
    {
        try
        {
        	 String current = new java.io.File( "." ).getCanonicalPath();
             System.out.println("Current dir:"+current);
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
