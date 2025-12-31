#!/bin/sh

# for testing only
# keys are automatically generated and persisted when running the application

fname=key
if [ ! -z "$1" ]
then
    fname="$1"
fi

openssl genrsa -out "private$fname.pem" 2048
# PKCS#1 -> PKCS#8 (supported java keyspec)
openssl pkcs8 -in "private$fname.pem" -topk8 -nocrypt -out "private$fname-pkcs8.pem"
openssl rsa -in "private$fname.pem" -out "public$fname.pem" -pubout


# human readable
# openssl rsa -in privatekey.pem -text
# openssl rsa -in publickey.pem -text -pubin
