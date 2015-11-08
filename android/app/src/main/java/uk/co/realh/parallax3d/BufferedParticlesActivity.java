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

package uk.co.realh.parallax3d;

import thothbot.parallax.core.client.gl2.arrays.Float32Array;
import thothbot.parallax.core.shared.cameras.PerspectiveCamera;
import thothbot.parallax.core.shared.core.BufferAttribute;
import thothbot.parallax.core.shared.core.BufferGeometry;
import thothbot.parallax.core.shared.materials.Material;
import thothbot.parallax.core.shared.materials.PointCloudMaterial;
import thothbot.parallax.core.shared.math.Color;
import thothbot.parallax.core.shared.objects.PointCloud;
import thothbot.parallax.core.shared.scenes.Fog;

public class BufferedParticlesActivity extends DemoActivity {

    @Override
    protected DemoAnimatedScene getDemo()
    {
        return new DemoAnimatedScene()
        {
            PerspectiveCamera camera;
            PointCloud particleSystem;

            @Override
            protected void onStart()
            {
                camera = new PerspectiveCamera(
                        27, // fov
                        getRenderer().getAbsoluteAspectRation(), // aspect
                        5, // near
                        35000 // far
                );
                camera.getPosition().setZ(2750);


                getScene().setFog(new Fog(0x050505, 2000, 3500));

                //

                int particles = 500000;

                BufferGeometry geometry = new BufferGeometry();

                Float32Array positions = Float32Array.create(particles * 3);
                Float32Array colors = Float32Array.create(particles * 3);

                Color color = new Color();
                double n = 1000, n2 = n / 2;   // particles spread in the cube

                for (int i = 0; i < positions.getLength(); i += 3)
                {

                    // positions
                    double x = Math.random() * n - n2;
                    double y = Math.random() * n - n2;
                    double z = Math.random() * n - n2;

                    positions.set(i, x);
                    positions.set(i + 1, y);
                    positions.set(i + 2, z);


                    // colors
                    double vx = (x / n) + 0.5;
                    double vy = (y / n) + 0.5;
                    double vz = (z / n) + 0.5;

                    color.setRGB(vx, vy, vz);

                    colors.set(i, color.getR());
                    colors.set(i + 1, color.getG());
                    colors.set(i + 2, color.getB());
                }

                geometry.addAttribute("position", new BufferAttribute(positions, 3));
                geometry.addAttribute("color", new BufferAttribute(colors, 3));

                geometry.computeBoundingSphere();


                //
                PointCloudMaterial material = new PointCloudMaterial();
                material.setVertexColors(Material.COLORS.VERTEX);
                material.setSize(15.0);

                particleSystem = new PointCloud(geometry, material);
                getScene().add(particleSystem);

                //

                getRenderer().setClearColor(getScene().getFog().getColor(), 1);
            }

            @Override
            protected void onUpdate(double duration)
            {
                double time = duration * 0.001;

                particleSystem.getRotation().setX(time * 0.25);
                particleSystem.getRotation().setY(time * 0.5);
                getRenderer().render(getScene(), camera);
            }
        };
    }

}
