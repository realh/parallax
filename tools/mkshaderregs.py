#!/usr/bin/env python3

import os, sys

shaders_dir = os.path.join(os.path.dirname(os.path.dirname(sys.argv[0])),
        'src', 'thothbot', 'parallax', 'core', 'client', 'shaders')

HEADER = """/*
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

package thothbot.parallax.core.client.shaders;

import java.util.HashMap;

public class %s
{
    private static HashMap<String, String> shaderMap;

    public static void set(String k, String v)
    {
        if (shaderMap == null)
            shaderMap = new HashMap<String, String>();
        shaderMap.put(k, v);
    }

    public static String get(String k)
    {
        return shaderMap.get(k);
    }

    static
    {"""

class ShaderSourceRegistry(object):

    def __init__(self, name):
        global HEADER
        self.name = name
        self.java = HEADER % name

    def close_and_save(self):
        global shaders_dir
        self.java += "\n    }\n}"
        fp = open(os.path.join(shaders_dir, self.name + '.java'), 'w')
        fp.write(self.java)
        fp.close()

    def add_shader(self, name, source):
        source = '"' + source.strip().replace('\n', '\\n" +\n"') + '\\n"'
        self.java += '\n\n        set("%s",\n%s);' % (name, source)


vert_chunks = ShaderSourceRegistry('VertexShaderChunks')
frag_chunks = ShaderSourceRegistry('FragmentShaderChunks')
vert_sources = ShaderSourceRegistry('VertexShaderSources')
frag_sources = ShaderSourceRegistry('FragmentShaderSources')


def scan_directory(dirname):
    global vert_chunks, frag_chunks, vert_sources, frag_sources
    files = os.listdir(dirname)
    for f in files:
        fp = open(os.path.join(dirname, f), 'r')
        source = fp.read()
        fp.close()
        if f.endswith('_fragment.glsl'):
            frag_chunks.add_shader(f[:-14], source)
        elif f.endswith('_vertex.glsl'):
            vert_chunks.add_shader(f[:-12], source)
        elif f.endswith('.fs'):
            frag_sources.add_shader(f[:-3], source)
        elif f.endswith('.vs'):
            vert_sources.add_shader(f[:-3], source)


scan_directory(os.path.join(shaders_dir, 'chunk'))
scan_directory(os.path.join(shaders_dir, 'source'))

vert_chunks.close_and_save()
frag_chunks.close_and_save()
vert_sources.close_and_save()
frag_sources.close_and_save()

