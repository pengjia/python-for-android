#!/bin/bash

VERSION_tornado=2.3
URL_tornado=https://github.com/downloads/facebook/tornado/tornado-$VERSION_tornado.tar.gz
DEPS_tornado=(pycurl)
MD5_tornado=810c3ecd425924fbf0aa1fa040f93ad1
BUILD_tornado=$BUILD_PATH/tornado/$(get_directory $URL_tornado)
RECIPE_tornado=$RECIPES_PATH/tornado

function prebuild_tornado() {
	true
}

function build_tornado() {
	if [ -d "$BUILD_PATH/python-install/lib/python2.7/site-packages/tornado" ]; then
		return
	fi

	cd $BUILD_tornado

	push_arm

	export PYTHONPATH=$BUILD_hostpython/Lib/site-packages
        try $BUILD_hostpython/hostpython setup.py install -O2 --root=$BUILD_PATH/python-install --install-lib=lib/python2.7/site-packages
	try rm -rf $BUILD_PATH/python-install/lib/python*/site-packages/tornado/test

	pop_arm
}

function postbuild_tornado() {
	true
}
