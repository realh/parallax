package uk.co.realh.parallax;

import org.parallax3d.renderer.Float32Array;
import org.parallax3d.renderer.Uint16Array;
import org.parallax3d.cameras.PerspectiveCamera;
import org.parallax3d.core.BufferAttribute;
import org.parallax3d.core.BufferGeometry;
import org.parallax3d.lights.AmbientLight;
import org.parallax3d.lights.DirectionalLight;
import org.parallax3d.materials.Material;
import org.parallax3d.materials.MeshPhongMaterial;
import org.parallax3d.math.Color;
import org.parallax3d.math.Vector3;
import org.parallax3d.objects.Mesh;
import org.parallax3d.scenes.Fog;

public class BufferGeometryActivity extends DemoActivity {

    @Override
    protected DemoAnimatedScene getDemo()
    {
        return new DemoAnimatedScene()
        {
            PerspectiveCamera camera;
            Mesh mesh;

            @Override
            protected void onStart()
            {
                camera = new PerspectiveCamera(
                        27, // fov
                        getRenderer().getAbsoluteAspectRation(), // aspect
                        1, // near
                        3500 // far
                );

                camera.getPosition().setZ(2750);

                getScene().setFog(new Fog(0x050505, 2000, 3500));

                //

                getScene().add(new AmbientLight(0x444444));

                DirectionalLight light1 = new DirectionalLight(0xffffff, 0.5);
                light1.getPosition().set(1, 1, 1);
                getScene().add(light1);

                DirectionalLight light2 = new DirectionalLight(0xffffff, 1.5);
                light2.getPosition().set(0, -1, 0);
                getScene().add(light2);

                //

                int triangles = 160000;

                BufferGeometry geometry = new BufferGeometry();

                // break geometry into
                // chunks of 21,845 triangles (3 unique vertices per triangle)
                // for indices to fit into 16 bit integer number
                // floor(2^16 / 3) = 21845

                int chunkSize = 20000;

                Uint16Array indices = Uint16Array.create(triangles * 3);

                for (int i = 0; i < indices.getLength(); i++)
                {
                    indices.set(i, i % (3 * chunkSize));
                }

                Float32Array positions = Float32Array.create(triangles * 3 * 3);
                Float32Array normals = Float32Array.create(triangles * 3 * 3);
                Float32Array colors = Float32Array.create(triangles * 3 * 3);

                Color color = new Color();

                double n = 800, n2 = n / 2;   // triangles spread in the cube
                double d = 12, d2 = d / 2;   // individual triangle size

                Vector3 pA = new Vector3();
                Vector3 pB = new Vector3();
                Vector3 pC = new Vector3();

                Vector3 cb = new Vector3();
                Vector3 ab = new Vector3();

                for (int i = 0; i < positions.getLength(); i += 9)
                {
                    // positions

                    double x = Math.random() * n - n2;
                    double y = Math.random() * n - n2;
                    double z = Math.random() * n - n2;

                    double ax = x + Math.random() * d - d2;
                    double ay = y + Math.random() * d - d2;
                    double az = z + Math.random() * d - d2;

                    double bx = x + Math.random() * d - d2;
                    double by = y + Math.random() * d - d2;
                    double bz = z + Math.random() * d - d2;

                    double cx = x + Math.random() * d - d2;
                    double cy = y + Math.random() * d - d2;
                    double cz = z + Math.random() * d - d2;

                    positions.set(i, ax);
                    positions.set(i + 1, ay);
                    positions.set(i + 2, az);

                    positions.set(i + 3, bx);
                    positions.set(i + 4, by);
                    positions.set(i + 5, bz);

                    positions.set(i + 6, cx);
                    positions.set(i + 7, cy);
                    positions.set(i + 8, cz);

                    // flat face normals

                    pA.set(ax, ay, az);
                    pB.set(bx, by, bz);
                    pC.set(cx, cy, cz);

                    cb.sub(pC, pB);
                    ab.sub(pA, pB);
                    cb.cross(ab);

                    cb.normalize();

                    double nx = cb.getX();
                    double ny = cb.getY();
                    double nz = cb.getZ();

                    normals.set(i, nx);
                    normals.set(i + 1, ny);
                    normals.set(i + 2, nz);

                    normals.set(i + 3, nx);
                    normals.set(i + 4, ny);
                    normals.set(i + 5, nz);

                    normals.set(i + 6, nx);
                    normals.set(i + 7, ny);
                    normals.set(i + 8, nz);

                    // colors

                    double vx = (x / n) + 0.5;
                    double vy = (y / n) + 0.5;
                    double vz = (z / n) + 0.5;

                    //color.setHSV( 0.5 + 0.5 * vx, 0.25 + 0.75 * vy, 0.25 + 0.75 * vz );
                    color.setRGB(vx, vy, vz);

                    colors.set(i, color.getR());
                    colors.set(i + 1, color.getG());
                    colors.set(i + 2, color.getB());

                    colors.set(i + 3, color.getR());
                    colors.set(i + 4, color.getG());
                    colors.set(i + 5, color.getB());

                    colors.set(i + 6, color.getR());
                    colors.set(i + 7, color.getG());
                    colors.set(i + 8, color.getB());

                }

                geometry.addAttribute("index", new BufferAttribute(indices, 1));
                geometry.addAttribute("position", new BufferAttribute(positions, 3));
                geometry.addAttribute("normal", new BufferAttribute(normals, 3));
                geometry.addAttribute("color", new BufferAttribute(colors, 3));

                int offsets = triangles / chunkSize;

                for (int i = 0; i < offsets; i++)
                {

                    BufferGeometry.DrawCall drawcall = new BufferGeometry.DrawCall(
                            i * chunkSize * 3, // start
                            Math.min(triangles - (i * chunkSize), chunkSize) * 3, // count
                            i * chunkSize * 3 //index
                    );

                    geometry.getDrawcalls().add(drawcall);

                }

                geometry.computeBoundingSphere();

                MeshPhongMaterial material = new MeshPhongMaterial();

                material.setColor(new Color(0xaaaaaa));
                material.setAmbient(new Color(0xaaaaaa));
                material.setSpecular(new Color(0xffffff));
                material.setShininess(250);
                material.setSide(Material.SIDE.DOUBLE);
                material.setVertexColors(Material.COLORS.VERTEX);

                mesh = new Mesh(geometry, material);
                getScene().add(mesh);

                //

                getRenderer().setClearColor(getScene().getFog().getColor(), 1);
                getRenderer().setGammaInput(true);
                getRenderer().setGammaOutput(true);
            }

            @Override
            protected void onUpdate(double duration)
            {
                double time = duration * 0.001;

                mesh.getRotation().setX(time * 0.25);
                mesh.getRotation().setY(time * 0.5);

                getRenderer().render(getScene(), camera);
            }
        };
    }
}
