#!/usr/bin/env bash
terraform plan -var "do_token=${DO_PAT}" -var "pvt_key=$DO_KEY_FILE"
