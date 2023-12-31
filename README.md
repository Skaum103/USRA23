# USRA23
Repository for USRA 2023 research project on VPN encrypted traffic generation & analysis.

# Project Description
This project aims to generate traffic with four applications and analyze it to determine the application, collect location and if a VPN service is used. The project will also attempt to determine the VPN provider used. The project will be implemented in Java, shell and R.

**Target applications**: Slack (Instant Messaging), Twitch (Video Streaming), Chrome (Browsing), Google Drive (Cloud Storgage Service)

**VPN service used in this study**: CloudFlare 1.1.1.1 WARP

## Usage
### Traffic Generation
Libraries, applications and packages required:
Streamlink, VLC, CHrome, Selenium, Chromedriver, gDrive, Slack, Twitch, Google Drive, R, RStudio

Set up the following applications:
1. Slack
    - Get a Slack API token from [here](https://api.slack.com/custom-integrations/legacy-tokens)
    - The token should have the permission to read and post in a channel
    - Put the token in `token.txt`
2. Twitch
    - Put any Twitch Channel URLs in `channels.csv`
    - The URLs in the csv file should be in the format `1,twitch.tv/<channel_name>`
    - Install the tool Streamlink from [here](https://streamlink.github.io/install.html)
    - Install VLC from [here](https://www.videolan.org/vlc/index.html)
    - Make sure you can run Streamlink and VLC from the command line
3. Chrome
    - Install Selenium and Chromedriver
4. Google Drive
    - Install gDrive3 tool from [here](https://github.com/glotlabs/gdrive)
    - Set up gDrive with a Google account
    - Follow the instructions to set up gDrive3 [here](https://github.com/glotlabs/gdrive)
    - Create a folder in Google Drive and get the folder ID, it will be required when run the script