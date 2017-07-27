package fly2cam;

import group1.IPixel;

public interface IAutoExposure
{
    int exposureBoost(IPixel[][] pixels);
    int shutterBoost(IPixel[][] pixels);
    int gainBoost(IPixel[][] pixels);
}
