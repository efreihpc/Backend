__kernel void
  MonteCarloKernel(   __global const double drift,
                      __global const double deviation,
                      __global const uint *randomseedX,
                      __global const uint *randomseedY,
                      __global const double begin,
                      __global const int numberOfDays,
                      __global double *result)
  {
    int tid = get_global_id(0);

    int day = 0;

    for (int day = 0; day < numberOfDays; day = day + 2)
    {
        uint seed = randomseedX[tid] + iSample % tid;
        uint t = seed ^ (seed << 11);
        uint rnd_src = randomseedY[tid] ^ (randomseedY[tid] >> 19) ^ (t ^ (t >> 8));
        double rnd_num = rnd_src*cos(2.0*M_PI*rnd_src);

        seed = randomseedX[tid] + tid % iSample;
        t = seed ^ (seed << 11);
        rnd_src = randomseedY[tid] ^ (randomseedY[tid] >> 19) ^ (t ^ (t >> 8));
        double rnd_num1 = rnd_src*sin(2.0*M_PI*rnd_src);

        float2 random = BoxMuller(float2(rnd_num, rnd_num1));

        double last;
        if(day == 0)
          last = begin;
        else
          last = result[day -1]

        result[i] = last*exp(drift+deviation*random.x);
        result[i + 1] = result[i]*exp(drift+deviation*random.y);

    }
  }

    float2 BoxMuller(float2 uniform)
    {
        double r = sqrt(-2 * log(uniform.x));
        double theta = 2 * PI * uniform.y;
        return (float2)(r * sin(theta), r * cos(theta));
    }