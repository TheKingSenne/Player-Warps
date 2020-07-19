#!/usr/bin/env bash

if [[ "$TRAVIS_PULL_REQUEST" == "false" ]]; then

  echo -e "Running release script...\n"
  echo -e "Publishing javadocs and artifacts...\n"
  cd $HOME

  rsync -r --quiet -e "ssh -p 2222 -o StrictHostKeyChecking=no" \
  $HOME/build/internetpolice-eu/PWarp/target/mvn-repo/ \
  travis@travis.internetpolice.eu:WWW/repo/

  echo -e "Publishing final plugin release...\n"

  rsync -r --quiet -e "ssh -p 2222 -o StrictHostKeyChecking=no" \
  $HOME/build/internetpolice-eu/PWarp/target/PWarp-*.jar \
  travis@travis.internetpolice.eu:WWW/downloads/PWarp/

fi
