#ifndef M_PI
#define M_PI 3.14159265358979
#endif

double2 boxMuller(double2 uniform)
{
    double r = sqrt(-2 * log(uniform.x));
    double theta = 2 * M_PI * uniform.y;
    return r * sin(theta), r * cos(theta);
}

uint2 randomNumber(uint2 randmonSeed)
{
  uint2 result;
  uint seed = randmonSeed.x + get_global_id(0);
  uint t = seed ^ (seed << 11);
  result.x = randmonSeed.y ^ (randmonSeed.y >> 19) ^ (t ^ (t >> 8));

  seed = randmonSeed.x + get_global_id(1);
  t = seed ^ (seed << 11);
  result.y = randmonSeed.y ^ (randmonSeed.y >> 19) ^ (t ^ (t >> 8));

  return result;
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
        uint2 rnd;
        rnd = randomNumber((randomseedX[tid], randomseedY[tid]));
        double2 normalizedRandom = boxMuller(((double)rnd.x, (double)rnd.y));

        double last;
        if(day == 0)
          last = begin;
        else
          last = result[day - 1];

        // result[day] = last*exp(drift+deviation*normalizedRandom.x);
        // result[day + 1] = result[day]*exp(drift+deviation*normalizedRandom.y);
        result[day] = drift+deviation*normalizedRandom.x;
        result[day + 1] = normalizedRandom.x;

    }
  }