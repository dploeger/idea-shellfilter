<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# Shell Filter Plugin Changelog

## [Unreleased]

- Updated to version 2 of IntelliJ platform template
- Support for 2024.2 versions

## [5.0.0] - 2024-03-22

### Added

- BREAKING CHANGE: Rebased the plugin code on the IntelliJ platform template
- Support for 2024.1 versions
- Support for Signed plugin

## [4.7.0]

### Added

- Updated framework to work with newer IntelliJ versions and to be able to reload the plugin without IDE restart. *This version includes deprecated support for the old configuration style. You need to install this version to be able to continue using your configuration in the next major version of this plugin.*

## [4.6.2]

### Fixed

- Support for current IntelliJ version

## [4.6.1]

### Fixed

- Fixed deprecations

## [4.6.0]

### Added

- Support for current IntelliJ version

## [4.5.0]

### Added

- Support for current IntelliJ version

## [4.4.0]

### Added

- Support for current IntelliJ version

## [4.3.0]

### Added

- Support for current IntelliJ version

## [4.2.0]

### Added

- Support for current IntelliJ version

## [4.1.0]

### Added

- Support for current IntelliJ version

## [4.0.0]

### Added

- Support for current IntelliJ version

## [3.0.3]

### Fixed

- Missed changelog on 3.0.2 (again :/)

## [3.0.2]

### Fixed

- Removed deprecated API call to Icon.getLoader

## [3.0.1]

### Fixed

- Missed changelog on 3.0.0

## [3.0.0]

### Added

- Removed deprecated API calls to be able to support the plugin for future IDEs

## [2.1.0]

### Added

- Added feature to directly enter a custom command using default shortcut CTRL+SHIFT+I

## [2.0.0]

### Added

- When no text is selected, the complete file will be sent to STDIN

## [1.0.1]

### Fixed

- Added default shortcut key CTRL-Meta-I

## [1.0.0]

### Added

- First stable release

## [1.0.0-beta.3]

### Fixed

- Smaller command dialog fixes and documentation update

## [1.0.0-beta.2]

### Fixed

- Optimized shell command handling
- Optimized command settings dialog

## [1.0.0-beta.1]

### Added

- First version, adopted code from [old shell-process plugin](https://code.google.com/archive/p/shell-process/)

[Unreleased]: https://github.com/dploeger/idea-shellfilter/compare/v5.0.0...HEAD
[5.0.0]: https://github.com/dploeger/idea-shellfilter/compare/0c47cdac24c84b3aec9aa8a986e2bef29e4e67e4...v5.0.0
[4.7.0]: https://github.com/dploeger/idea.shellfilter/compare/v4.6.2...v4.7.0
[4.6.2]: https://github.com/dploeger/idea.shellfilter/compare/v4.6.1...v4.6.2
[4.6.1]: https://github.com/dploeger/idea.shellfilter/compare/v4.6.0...v4.6.1
[4.6.0]: https://github.com/dploeger/idea.shellfilter/compare/v4.5.0...v4.6.0
[4.5.0]: https://github.com/dploeger/idea.shellfilter/compare/v4.4.0...v4.5.0
[4.4.0]: https://github.com/dploeger/idea.shellfilter/compare/v4.3.0...v4.4.0
[4.3.0]: https://github.com/dploeger/idea.shellfilter/compare/v4.2.0...v4.3.0
[4.2.0]: https://github.com/dploeger/idea.shellfilter/compare/v4.1.0...v4.2.0
[4.1.0]: https://github.com/dploeger/idea.shellfilter/compare/v4.0.0...v4.1.0
[4.0.0]: https://github.com/dploeger/idea.shellfilter/compare/v3.0.3...v4.0.0
[3.0.3]: https://github.com/dploeger/idea.shellfilter/compare/v3.0.2...v3.0.3
[3.0.2]: https://github.com/dploeger/idea.shellfilter/compare/v3.0.1...v3.0.2
[3.0.1]: https://github.com/dploeger/idea.shellfilter/compare/v3.0.0...v3.0.1
[3.0.0]: https://github.com/dploeger/idea.shellfilter/compare/v2.1.0...v3.0.0
[2.1.0]: https://github.com/dploeger/idea.shellfilter/compare/v2.0.0...v2.1.0
[2.0.0]: https://github.com/dploeger/idea.shellfilter/compare/v1.0.1...v2.0.0
[1.0.1]: https://github.com/dploeger/idea.shellfilter/compare/v1.0.0...v1.0.1
[1.0.0]: https://github.com/dploeger/idea.shellfilter/compare/v1.0.0-beta.3...v1.0.0
[1.0.0-beta.1]: https://github.com/dploeger/idea.shellfilter/commits/v1.0.0-beta.1
[1.0.0-beta.2]: https://github.com/dploeger/idea.shellfilter/compare/v1.0.0-beta.1...v1.0.0-beta.2
[1.0.0-beta.3]: https://github.com/dploeger/idea.shellfilter/compare/v1.0.0-beta.2...v1.0.0-beta.3
