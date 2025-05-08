#!/usr/bin/env bash
terraform apply -var "do_token=${DO_PAT}" -var "pvt_key=$DO_KEY_FILE"
