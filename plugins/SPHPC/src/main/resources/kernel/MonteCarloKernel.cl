#ifndef M_PI
#define M_PI 3.14159265358979
#endif

float2 boxMuller(float2 uniform)
{
    double r = sqrt(-2 * log(uniform.x));
    double theta = 2 * M_PI * uniform.y;
    return (float2)(r * sin(theta), r * cos(theta));
}

kernel void
  MonteCarloKernel(   double drift,
                      double deviation,
                      double begin,
                      int numberOfDays,
                      global const uint *randomseedX,
                      global const uint *randomseedY,
                      global double *result)
  {
    int tid = get_global_id(0);

    int day = 0;

    for (int day = 0; day < numberOfDays; day = day + 2)
    {
        uint seed = randomseedX[tid] + day % tid;
        uint t = seed ^ (seed << 11);
        uint rnd_src = randomseedY[tid] ^ (randomseedY[tid] >> 19) ^ (t ^ (t >> 8));
        double rnd_num = rnd_src*cos(2.0*M_PI*rnd_src);

        seed = randomseedX[tid] + tid % day;
        t = seed ^ (seed << 11);
        rnd_src = randomseedY[tid] ^ (randomseedY[tid] >> 19) ^ (t ^ (t >> 8));
        double rnd_num1 = rnd_src*sin(2.0*M_PI*rnd_src);

        float2 random = boxMuller((float2)(rnd_num, rnd_num1));

        double last;
        if(day == 0)
          last = begin;
        else
          last = result[day -1];

        result[day] = last*exp(drift+deviation*random.x);
        result[day + 1] = result[day]*exp(drift+deviation*random.y);

    }
  }