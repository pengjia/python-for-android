#!/bin/bash

VERSION_libsqlite3=3.7.13
URL_libsqlite3=http://www.sqlite.org/sqlite-autoconf-3071300.tar.gz
DEPS_libsqlite3=()
MD5_libsqlite3=c97df403e8a3d5b67bb408fcd6aabd8e
BUILD_libsqlite3=$BUILD_PATH/libsqlite3/$(get_directory $URL_libsqlite3)
RECIPE_libsqlite3=$RECIPES_PATH/libsqlite3

function prebuild_libsqlite3() {
	true
}

function build_libsqlite3() {
	cd $BUILD_libsqlite3

        if [ -f .libs/libsqlite3.a ]; then
		return
	fi

	push_arm

       	try ./configure --build=i686-pc-linux-gnu --host=arm-linux-eabi
       	try make

	pop_arm
}

function postbuild_libsqlite3() {
	true
}
