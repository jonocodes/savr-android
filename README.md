# Savr

Savr is an app for saving online content to read later. It is file-centric, offline first, future proof, and favors decentralization. Read about the design and motivation in the FAQ below.

This repository is for the android app.

![screenshot](./screenshots/screenshots.png)


## Features
* save articles for reading later
* removes distractions like advertisements
* read content without an internet connection
* share articles with friends
* no dependency on a service/company to do the scraping or storage
* scraped content works well outside the app (plain html and images)
* plays well with file synchronization across devices
    * BYOB (bring your own backend) if you want. For example syncthing or Google Drive.


## Current state of development

Basic features have been implemented (scraping, viewing, sharing, archiving, dark theme), but I would consider this in alpha. It still has a bunch of rough edges.


# Installation

Download latest apk from here:
https://github.com/jonocodes/savr-android/releases/latest


# Development

Uses Kotlin and depends heavily on the storage access framework and android WebView.

More info TBD...


## FAQ

### Why another read-it-later app?

I consider myself a self-hosting enthusiast, who does not like to self host :) . I love open source and open formats, but I dont think every single purpose app should require a custom backend for it.

After using Pocket for 10+ years I decided it was time to take control of my own content collection. But why does Pocket need a special backend? Yes, it helps scrape the articles, but for the most part its just an API that handles authorization and storing content. Moving the scraping into the mobile app makes the backend no longer necessary.

Good examples of apps that work well with filesystems and open data formats are Obsidian (for notes) and Keepass (for passwords). You can run them on mobile, or desktop. All the functionality is in the app and a server is not required.

Of course you can bring in a sync service if you want, but its up to you how you want to store things. Syncing would happens outside the app, which adds flexibility. My preference is to use syncthing which provides a decentralized solution to sync data across my devices/machines.

Generally, __I would like more apps that exist in this space__. Like:
calendar, contacts, bookmark manager, and yes another todo app.

### Why not used other open source projects?

There are some great projects like Wallabag and Omnivore, but they require centralized hosting. Doing away with the server lets you not have to worry about: security, certificates, redundancy, uptime, firewalls, authorization, and all the other things that come with system administration.

### Is there a iOS app?

We need to figure out how to deal with the filesystem restrictions. Possibly use OS-provided storage providers ala keepassium: https://github.com/keepassium/KeePassium#automatic-sync

### Is there a desktop app?

A web app, API and electron desktop app are in the works.

