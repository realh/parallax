Parallax
=============

#### Android 3D library ####

Parallax is a 3D library for Java. The [original version](http://parallax3d.org)
uses [Google Web Toolkit](https://developers.google.com/web-toolkit/), the
Java to JavaScript cross-compiler. GWT allows developers to write Java code
and convert it to standard JavaScript during compilation.

Parallax's API and shaders are based on
[three.js](http://github.com/mrdoob/three.js).

The purpose of this fork is to port Parallax to Android, and shortly also for
use with [libGDX](https://libgdx.badlogicgames.com/).
It's still at an early stage and incomplete, but the core features are now
working in Android.

Unfortunately, mainly due to limitations of Java, and the specialist nature of
GWT, it will probably never be possible to unify these versions without changing
the original API, compromising efficiency with forwarding functions, and/or
using JNI extensively.

## How to use the code ###

The repository basically adds an Android Studio app in the `android` folder,
implementing the demo app, which is now being submitted to Google Play. To use
the library you will probably just want to add
`android/app/src/main/java/thothbot` to your own project. Please note the
package namespace and its internal structure are highly likely to change in the
near future.

### Upstream Documentation ###

Stable version
[API Reference](http://thothbot.github.com/parallax/docs/index.html) 
| Dev [API Reference](http://thothbot.github.com/parallax/docs/dev/) 

[Wiki](https://github.com/thothbot/parallax/wiki) 
| [Bugs](https://github.com/thothbot/parallax/issues)

