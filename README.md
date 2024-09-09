# <img src="src/main/resources/icons/icon@2x.png"> Shellfilter

IntelliJ IDEA plugin to filter source code using a shell script.

![Build](https://github.com/dploeger/idea-shellfilter/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/9958-shell-filter.svg)](https://plugins.jetbrains.com/plugin/9958-shell-filter)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/9958-shell-filter.svg)](https://plugins.jetbrains.com/plugin/9958-shell-filter)

## Introduction

<!-- Plugin description -->
This IntelliJ IDEA plugin allows running the currently selected text through an external shell script, enabling
advanced functions like filtering text, encrypting or decrypting, etc.
<!-- Plugin description end -->

## Installation

Install the plugin from the [Jetbrains Marketplace](https://plugins.jetbrains.com/plugin/9958-shell-filter).

## Configuration

In Settings/Tools/Shellfilter settings, a list of scripts can be configured that can be selected in the Shellfilter
popup. 

Additionally, the default shell to run the scripts from can be configured. Use "%s" in the command line which will
be replaced with the temporary file that holds your script content. If no "%s" is used, the name of the file will
simply be added to the command line.

## Usage

Select a text in the editor view, select the Shell Filter item from the Edit menu and select the script
to run.

In this pop up, a custom command can also be selected for adhoc-type of scripts.

After the command completes successfully, the selected text will be replaced with the script's output.

Selecting text is optional, so you can also use Shell Filter to quickly run scripts and get their output (i.e. for
generating passwords).