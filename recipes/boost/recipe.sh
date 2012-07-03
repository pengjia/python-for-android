#!/bin/bash

VERSION_boost=1.49.0
URL_boost=http://downloads.sourceforge.net/project/boost/boost/1.49.0/boost_1_49_0.tar.bz2
DEPS_boost=(python)
MD5_boost=0d202cb811f934282dea64856a175698
BUILD_boost=$BUILD_PATH/boost/$(get_directory $URL_boost)
RECIPE_boost=$RECIPES_PATH/boost

function prebuild_boost() {
	cd $BUILD_boost

        # check marker in our source build
        if [ -f .patched ]; then
                # no patch needed
                return
        fi

        try ./bootstrap.sh

        try cat >> $BUILD_boost/project-config.jam <<EOF

libraries = --with-date_time --with-filesystem --with-program_options --with-regex --with-signals --with-thread --with-python ;

option.set prefix : ./build ;
option.set exec-prefix : ./build/bin ;
option.set libdir : ./build/lib ;
option.set includedir : ./build/include ;
EOF

        try patch -p1 < $RECIPE_boost/patches/address_v6.ipp.patch
        try patch -p1 < $RECIPE_boost/patches/endian.hpp.patch
        try patch -p1 < $RECIPE_boost/patches/operations.cpp.patch
        try patch -p1 < $RECIPE_boost/patches/unversioned_layout.patch
        try patch -p1 < $RECIPE_boost/patches/workaround.hpp.patch
        try patch -p1 < $RECIPE_boost/patches/android.jam.patch
        try patch -p1 < $RECIPE_boost/patches/fenced_block.hpp.patch
        try patch -p1 < $RECIPE_boost/patches/socket_types.hpp.patch
        try patch -p1 < $RECIPE_boost/patches/user.hpp.patch
        try patch -p1 < $RECIPE_boost/patches/android.py.patch
        try patch -p1 < $RECIPE_boost/patches/name_generator.hpp.patch
        try patch -p1 < $RECIPE_boost/patches/string_generator.hpp.patch
        try patch -p1 < $RECIPE_boost/patches/v2_operations.cpp.patch

        try cat >> $BUILD_boost/tools/build/v2/user-config.jam <<EOF

using gcc : android : arm-linux-androideabi-gcc :
<compileflags>-Os
<compileflags>-O2
<compileflags>-g
<compileflags>-std=gnu++0x
<compileflags>-Wno-variadic-macros
<compileflags>-fexceptions
<compileflags>-fpic
<compileflags>-ffunction-sections
<compileflags>-funwind-tables
<compileflags>-march=armv5te
<compileflags>-mtune=xscale
<compileflags>-msoft-float
<compileflags>-mthumb
<compileflags>-fomit-frame-pointer
<compileflags>-fno-strict-aliasing
<compileflags>-finline-limit=64
<compileflags>-D__ARM_ARCH_5__
<compileflags>-D__ARM_ARCH_5T__
<compileflags>-D__ARM_ARCH_5E__
<compileflags>-D__ARM_ARCH_5TE__
<compileflags>-DANDROID
<compileflags>-D__ANDROID__
<compileflags>-DNDEBUG
<compileflags>-I${ANDROIDNDK}/platforms/android-14/arch-arm/usr/include
<compileflags>-I${ANDROIDNDK}/sources/cxx-stl/gnu-libstdc++/include
<compileflags>-I${ANDROIDNDK}/sources/cxx-stl/gnu-libstdc++/libs/armeabi-v7a/include
<linkflags>-nostdlib
<linkflags>-lc
<linkflags>-L${ANDROIDNDK}/sources/cxx-stl/gnu-libstdc++/libs/armeabi-v7a
# Flags above are for android
<architecture>arm
<compileflags>-fvisibility=hidden
<compileflags>-fvisibility-inlines-hidden
<compileflags>-fdata-sections
<cxxflags>-frtti
<cxxflags>-D__arm__
<cxxflags>-D_REENTRANT
<cxxflags>-D_GLIBCXX__PTHREADS
<compileflags>-I${BUILD_python}/Include
<linkflags>-L${BUILD_python}
;
EOF
        # everything done, touch the marker !
        touch .patched
}

function build_boost() {
	cd $BUILD_boost

        if [ -f build/lib/libboost_system.a ]; then
		return
	fi

        push_arm

       	try ./bjam cxxflags="$CXXFLAGS" --disable-filesystem3 define=BOOST_FILESYSTEM_VERSION=2 install

	pop_arm
}

function postbuild_boost() {
	true
}
