Parallax
=============

#### Android 3D library ####

Parallax is a 3D library for Java. The [original version](http://parallax3d.org)
uses [Google Web Toolkit](https://developers.google.com/web-toolkit/), the
Java to JavaScript cross-compiler. GWT allows developers to write Java code
and convert it to standard JavaScript during compilation.

Parallax's API and shaders are based on [three.js](http://github.com/mrdoob/three.js).

This fork is an attempt to port Parallax 3D to Android, and hopefully also for use
with [LWJGL](http://www.lwjgl.org/) and/or [libGDX](https://libgdx.badlogicgames.com/).
It's still at a very early stage and nowhere near usable.

Unfortunately, mainly due to limitations of Java, and the specialist nature of
GWT, it will probably never be possible to unify these versions without changing
the original API, compromising efficiency and/or using JNI extensively.

### Documentation ###

Stable version [API Reference](http://thothbot.github.com/parallax/docs/index.html) 
| Dev [API Reference](http://thothbot.github.com/parallax/docs/dev/) 

[Wiki](https://github.com/thothbot/parallax/wiki) 
| [Bugs](https://github.com/thothbot/parallax/issues)

