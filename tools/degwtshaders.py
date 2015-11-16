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

import os, re, sys

chunk_re = re.compile( \
        '^(\s*)@Source\("shaders/([a-z_]+)_(fragment|vertex)\.glsl"\)')
source_re = re.compile( \
        '^(\s*)@Source\("shaders/([A-Za-z_]+)\.([fv])s"\)')

pkg_name = 'thothbot.parallax.plugins.postprocessing.shaders'


def process_java(filename):
    global chunk_re, source_re

    fp = open(filename, 'r')
    lines_in = fp.readlines()
    fp.close()
    print("Read ", filename)
    l = len(lines_in)

    lines_out = []

    n = 0
    while n < l:
        ln = lines_in[n]
        ln = ln.replace('interface Resources extends DefaultResources',
                'static class Resources extends DefaultResources')
        ln = ln.replace('Resources INSTANCE = GWT.create(Resources.class)',
                'static Resources INSTANCE = new Resources()')
        if ln.startswith('import com.google.gwt.'):
            n += 1
            continue

        chunk_match = chunk_re.search(ln)
        source_match = source_re.search(ln)
        if chunk_match or source_match:
            n += 1
            ln = lines_in[n]
            if chunk_match:
                stype = chunk_match.group(3)
                sname = chunk_match.group(2)
                if sname == 'default':
                    sname = 'default_shader'
                spc = chunk_match.group(1)
                ln = ln.replace('TextResource ', 'String ').replace(';', '')
                lines_out.append(ln)
                lines_out.append("%s{\n" % spc)
                lines_out.append("%s\treturn %s.chunk.%s.%s;\n" % \
                        (spc, pkg_name, sname, stype))
                lines_out.append("%s}\n" % spc)
            if source_match:
                if source_match.group(3) == 'f':
                    mstub = 'Fragment'
                else:
                    mstub = 'Vertex'
                stype = mstub.lower()
                sname = source_match.group(2)
                if sname == 'default':
                    sname = 'default_shader'
                spc = source_match.group(1)
                lines_out.append('%s@Override\n' % spc)
                lines_out.append('%spublic String get%sShader()\n' % \
                        (spc, mstub))
                lines_out.append("%s{\n" % spc)
                lines_out.append("%s\treturn %s.source.%s.%s;\n" % \
                        (spc, pkg_name, sname, stype))
                lines_out.append("%s}\n" % spc)
        elif ln.startswith(' * This file is part of Parallax project.'):
            lines_out.append( \
            ' * This file is part of the realh fork of the Parallax project.\n')
        else:
            lines_out.append(ln)
            if ln.startswith(' * Copyright') and not 'Houghton' in ln:
                lines_out.append( \
                        ' * Copyright 2015 Tony Houghton, h@realh.co.uk\n')

        n += 1

    fp = open(filename, 'w')
    fp.writelines(lines_out)
    fp.close()
    print("Wrote ", filename)


for f in sys.argv[1:]:
    process_java(f)
