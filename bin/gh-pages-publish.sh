#!/usr/bin/env bash

set -e

ROOT_DIR=$([[ $PWD == */bin ]] && dirname $PWD || echo $PWD)

TEMP_DIR=/tmp/scala-collections-benchmark
BENCH_DIR=${ROOT_DIR}/target/benchmarks

rm -rf ${TEMP_DIR}

mkdir -p ${TEMP_DIR}/results

cp -r ${BENCH_DIR}/report/* ${TEMP_DIR}/
cp ${BENCH_DIR}/*.dsv       ${TEMP_DIR}/results
cp ${BENCH_DIR}/*.json.gz   ${TEMP_DIR}/results

sed -i 's/\.\./\.\.\\\/scala-collections-benchmark\\\/results/g' ${TEMP_DIR}/js/ScalaMeter/data.js

cd ${TEMP_DIR}

git init
git remote add origin git@github.com:l15k4/scala-collections-benchmark.git
git checkout --orphan gh-pages
git add .
git commit -m "benchmark-results"
git push origin gh-pages -f
