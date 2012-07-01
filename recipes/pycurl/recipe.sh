#!/bin/bash

VERSION_pycurl=7.19.0
URL_pycurl=http://pycurl.sourceforge.net/download/pycurl-$VERSION_pycurl.tar.gz
DEPS_pycurl=(libcurl hostpython python)
MD5_pycurl=919d58fe37e69fe87ce4534d8b6a1c7b
BUILD_pycurl=$BUILD_PATH/pycurl/$(get_directory $URL_pycurl)
RECIPE_pycurl=$RECIPES_PATH/pycurl

function prebuild_pycurl() {
	true
}

function build_pycurl() {

        if [ -d "$BUILD_PATH/python-install/lib/python2.7/site-packages/pycurl" ]; then
                return
        fi

        cd $BUILD_pycurl

        push_arm

        export CC="$CC -I$BUILD_libcurl/include"
        export LDFLAGS="$LDFLAGS -L$LIBS_PATH -L$BUILD_libcurl"
	export PATH="$BUILD_libcurl:$PATH"

	try sed -i s:prefix=\/usr\/local:prefix=$BUILD_libcurl: $BUILD_libcurl/curl-config
	try sed -i s:libcurl.a:.libs/libcurl.a: $BUILD_libcurl/curl-config
	try sed -i s:-lcurl:: $BUILD_libcurl/curl-config
	try chmod +x $BUILD_libcurl/curl-config

        try $BUILD_PATH/python-install/bin/python.host setup.py build_ext -v
        try find build/lib.* -name "*.o" -exec $STRIP {} \;

        try $BUILD_PATH/python-install/bin/python.host setup.py install -O2

        pop_arm
}

function postbuild_pycurl() {
	true
}
