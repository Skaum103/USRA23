#!/bin/sh
tshark -i en0 -a duration:3600 -w ~/cap.pcapng