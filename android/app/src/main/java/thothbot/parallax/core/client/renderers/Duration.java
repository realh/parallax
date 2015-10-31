/*
 * Copyright 2015 Tony Houghton, h@realh.co.uk
 * 
 * This file is part of the realh fork of the Parallax project.
 * 
 * Parallax is free software: you can redistribute it and/or modify it 
 * under the terms of the Creative Commons Attribution 3.0 Unported License.
 * 
 * Parallax is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the Creative Commons Attribution 
 * 3.0 Unported License. for more details.
 * 
 * You should have received a copy of the the Creative Commons Attribution 
 * 3.0 Unported License along with Parallax. 
 * If not, see http://creativecommons.org/licenses/by/3.0/.
 */

package thothbot.parallax.core.client.renderers;

/**
 * Mimics GWT's Duration class.
 */
public final class Duration
{
    long startTime;

    public Duration()
    {
        startTime = java.lang.System.currentTimeMillis();
    }

    public void reset()
    {
        startTime = java.lang.System.currentTimeMillis();
    }

    static double currentTimeMillis()
    {
        return java.lang.System.currentTimeMillis();
    }

    int	elapsedMillis()
    {
        return (int) (System.currentTimeMillis() - startTime);
    }

    double getStartMillis()
    {
        return startTime;
    }


}
