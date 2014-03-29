#!/bin/bash

VERSION_libcurl=7.26.0
URL_libcurl=http://curl.haxx.se/download/curl-$VERSION_libcurl.tar.gz
DEPS_libcurl=()
MD5_libcurl=3fa4d5236f2a36ca5c3af6715e837691
BUILD_libcurl=$BUILD_PATH/libcurl/$(get_directory $URL_libcurl)
RECIPE_libcurl=$RECIPES_PATH/libcurl

function prebuild_libcurl() {
	true
}

function build_libcurl() {
	cd $BUILD_libcurl

        if [ -f lib/.libs/libcurl.a ]; then
		return
	fi

	push_arm

       	try ./configure --build=i686-pc-linux-gnu --host=arm-linux-eabi
       	try make

	pop_arm
}

function postbuild_libcurl() {
	true
}
