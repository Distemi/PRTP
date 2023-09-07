package xyz.distemi.prtp.data;

import xyz.distemi.prtp.data.antimations.IProfileAnimation;

public class Profile {
    public String name;
    public String world;
    public int minimalRadius = 0;
    public int radius;
    public String target;
    public String cost;
    public IProfileAnimation animation;


    @Override
    public String toString() {
        return "Profile{" +
                "name='" + name + '\'' +
                ", world='" + world + '\'' +
                ", minimalRadius=" + minimalRadius +
                ", radius=" + radius +
                ", target='" + target + '\'' +
                ", cost='" + cost + '\'' +
                ", animation=" + animation +
                '}';
    }
}
