#!/usr/bin/env python3

# Copyright 2015 Tony Houghton, h@realh.co.uk
# 
# This file is part of the realh fork of the Parallax project.
# 
# Parallax is free software: you can redistribute it and/or modify it 
# under the terms of the Creative Commons Attribution 3.0 Unported License.
# 
# Parallax is distributed in the hope that it will be useful, but 
# WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
# or FITNESS FOR A PARTICULAR PURPOSE. See the Creative Commons Attribution 
# 3.0 Unported License. for more details.
# 
# You should have received a copy of the the Creative Commons Attribution 
# 3.0 Unported License along with Parallax. 
# If not, see http://creativecommons.org/licenses/by/3.0/.

import os, sys

# package thothbot.parallax.core.client.shaders.%s;
TEMPLATE_BOTH = \
"""// Generated from %s and %s

package thothbot.parallax.plugins.postprocessing.shaders.%s;

public class %s
{
    public static final String vertex =
%s;

    public static final String fragment =
%s;
}
"""

TEMPLATE_VERT = \
"""// Generated from %s

package thothbot.parallax.plugins.postprocessing.shaders.%s;

public class %s
{
    public static final String vertex =
%s;
}
"""

TEMPLATE_FRAG = \
"""// Generated from %s

package thothbot.parallax.plugins.postprocessing.shaders.%s;


public class %s
{
    public static final String fragment =
%s;
}
"""

opj = os.path.join


#dest_dir = opj(os.path.dirname(os.path.dirname(sys.argv[0])),
#        'src', 'thothbot', 'parallax', 'core', 'client', 'shaders')
dest_dir = opj(os.path.dirname(os.path.dirname(sys.argv[0])),
        'android', 'app', 'src', 'main', 'java',
        'thothbot', 'parallax', 'plugins', 'postprocessing', 'shaders')
src_dir = opj(os.path.dirname(os.path.dirname(sys.argv[0])),
        'src', 'thothbot', 'parallax', 'plugins', 'postprocessing', 'shaders')


def make_jstring(dir_name, src_name):
    src_name = opj(dir_name, src_name)
    if not os.path.exists(src_name):
        return None
    fp = open(src_name, 'r')
    source = fp.read()
    fp.close()
    source = source.strip()
    source = source.replace('\n', '\\n" +\n"')
    source = source.replace('//"', '//')
    return '"' + source + '\\n"'


def glsl_to_java(src):
    global TEMPLATE, dest_dir, src_dir

    is_frag = False

    if src.endswith('.vs'):
        vert_src = src
        basename = src[:-3]
        frag_src = basename + '.fs'
        subdir = 'source'
    elif src.endswith('_vertex.glsl'):
        vert_src = src
        basename = src[:-12]
        frag_src = basename + '_fragment.glsl'
        subdir = 'chunk'
    elif src.endswith('.fs'):
        frag_src = src
        basename = src[:-3]
        vert_src = basename + '.vs'
        subdir = 'source'
        is_frag = True
    elif src.endswith('_fragment.glsl'):
        frag_src = src
        basename = src[:-14]
        vert_src = basename + '_vertex.glsl'
        subdir = 'chunk'
        is_frag = True
    else:
        raise Exception('Unrecognised filename "%s"' % src)

    src_d = opj(src_dir, subdir)
    dst_d = opj(dest_dir, subdir)

    # Can't have keyword as class name
    if basename == 'default':
        basename = 'default_shader'

    vert_jstring = make_jstring(src_d, vert_src)
    frag_jstring = make_jstring(src_d, frag_src)
    if is_frag:
        if vert_jstring:
            # Both exist, leave it to when src is for vertex
            return
        java = TEMPLATE_FRAG % (frag_src, subdir, basename, frag_jstring)
    elif frag_jstring:
        java = TEMPLATE_BOTH % (vert_src, frag_src, subdir, basename,
                vert_jstring, frag_jstring)
    else:
        java = TEMPLATE_VERT % (vert_src, subdir, basename, vert_jstring)
    
    fp = open(opj(dst_d, basename + '.java'), 'w')
    fp.write(java)
    fp.close()



def scan_directory(dirname):
    files = os.listdir(dirname)
    for f in files:
        glsl_to_java(f)


#scan_directory(os.path.join(src_dir, 'chunk'))
scan_directory(os.path.join(src_dir, 'source'))
