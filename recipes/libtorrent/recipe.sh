#!/bin/bash

VERSION_libtorrent=0.16.1
URL_libtorrent=http://libtorrent.googlecode.com/files/libtorrent-rasterbar-$VERSION_libtorrent.tar.gz
DEPS_libtorrent=(boost)
MD5_libtorrent=63dd7834d615deae2df55ebfebdddb8b
BUILD_libtorrent=$BUILD_PATH/libtorrent/$(get_directory $URL_libtorrent)
RECIPE_libtorrent=$RECIPES_PATH/libtorrent

function prebuild_libtorrent() {
        cd $BUILD_libtorrent

        # check marker in our source build
        if [ -f .patched ]; then
                # no patch needed
                return
        fi

        try patch -p1 < $RECIPE_libtorrent/patches/fix-android.patch

        # everything done, touch the marker !
        touch .patched
}

function build_libtorrent() {
	cd $BUILD_libtorrent

	if [ -f src/.libs/libtorrent-rasterbar.a ]; then
		return
	fi

        push_arm

        export CFLAGS="$CFLAGS -I$ANDROIDNDK/platforms/android-8/arch-arm/usr/include -I$ANDROIDNDK/sources/cxx-stl/gnu-libstdc++/include -I$ANDROIDNDK/sources/cxx-stl/gnu-libstdc++/include -I${ANDROIDNDK}/sources/cxx-stl/gnu-libstdc++/libs/armeabi-v7a/include -DANDROID"
        export LDFLAGS="$LDFLAGS -L$LIBS_PATH -L$ANDROIDNDK/sources/cxx-stl/gnu-libstdc++/libs/armeabi-v7a"
        export CFLAGS="$CFLAGS -I$BUILD_boost/build/include -I$BUILD_python/Inclue -I$BUILD_python"
        export LDFLAGS="$LDFLAGS -L$BUILD_boost/build/lib -L$BUILD_python"
        export CXXFLAGS="$CFLAGS"
        export PYTHON="$BUILD_python/hostpython"
        try ./configure --build=i686-pc-linux-gnu --host=arm-linux-eabi --with-boost=$BUILD_boost/build --disable-encryption --enable-python-binding --with-boost-python
        try make

        # build python extension
        cd bindings/python
        try $BUILD_PATH/python-install/bin/python.host setup.py build_ext -v
        try find build/lib.* -name "*.o" -exec $STRIP {} \;

        export PYTHONPATH=$BUILD_hostpython/Lib/site-packages
        try $BUILD_hostpython/hostpython setup.py install -O2 --root=$BUILD_PATH/python-install --install-lib=lib/python2.7/site-packages

        pop_arm
}

function postbuild_libtorrent() {
	true
}
