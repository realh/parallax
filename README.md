Parallax 
=============

#### Android and libGDX 3D library ####

Parallax is a 3D library for Java. The [original version](http://parallax3d.org)
uses [Google Web Toolkit](https://developers.google.com/web-toolkit/), the
Java to JavaScript cross-compiler. GWT allows developers to write Java code
and convert it to standard JavaScript during compilation.

Parallax's API and shaders are based on
[three.js](http://github.com/mrdoob/three.js).

The purpose of this fork is to port Parallax to Android and for use with
[libGDX](https://libgdx.badlogicgames.com/). The Android fork uses Android's
built-in libraries without the need for other third party libraries, but you can
also target Android with the libGDX fork if you prefer.

Both forks are still at an early stage and incomplete, but the core features
are now working.

Unfortunately, mainly due to limitations of Java, and the specialist nature of
GWT, it will probably not be practical to unify these versions without changing
the original API, compromising efficiency with forwarding functions, and/or
using JNI extensively.

## How to use the code ###

The 'master' branch is for Android. It basically adds an Android Studio app in
the `android` folder, implementing the demo app, which is [now available on
Google
Play](https://play.google.com/store/apps/details?id=realh.co.uk.parallax3d).
Please note that some of the demos use a lot of GPU and system memory so may
appear glitchy or crash even on fairly high end devices. The Parallax code is
in the top-level src directory in org/parallax3d. To use the library you will
probably just want to add that folder to your own project. It's symlinked into
the android app, so this may cause some difficulty on MS Windows.

The 'gdx' branch for libGDX is similar, except the gdx folder hierarchy contains
a libGDX demo app. Unfortunately I seem to have chosen a demo that doesn't work
properly with most desktop OpenGL drivers at 2.x level. The texture is also
upside-down, but that I hope to fix without forcing users to flip all their
image assets.

### Upstream Documentation ###

Stable version
[API Reference](http://thothbot.github.com/parallax/docs/index.html) 
| Dev [API Reference](http://thothbot.github.com/parallax/docs/dev/) 

[Wiki](https://github.com/thothbot/parallax/wiki) 
| [Bugs](https://github.com/thothbot/parallax/issues)

