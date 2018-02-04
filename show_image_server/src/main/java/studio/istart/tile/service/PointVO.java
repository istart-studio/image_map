package studio.istart.tile.service;

/**
 * @author DongYan
 * @version 1.0.0
 * @since 1.8
 */
public class PointVO
{
    private int x;
    private int y;
    private double lat;
    private double lon;

    private int topLeft;

    public int getX()
    {
        return x;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public int getY()
    {
        return y;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public double getLat()
    {
        return lat;
    }

    public void setLat(double lat)
    {
        this.lat = lat;
    }

    public double getLon()
    {
        return lon;
    }

    public void setLon(double lon)
    {
        this.lon = lon;
    }

    public int getTopLeft()
    {
        return topLeft;
    }

    public void setTopLeft(int topLeft)
    {
        this.topLeft = topLeft;
    }

}
