#!/bin/bash
. /root/.bashrc
`pwd`/spawn-fcgi -a 127.0.0.1 -p 40002 -f /usr/local/dimdim/Mediaserver/mods/interface.py>/dev/null
