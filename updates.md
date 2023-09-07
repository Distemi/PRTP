## Improvements, performance and fixes

ATTENTION: The configuration has been changed!

Changes:
- Added #T to message `teleported` which will be replaced by the time of teleportation in milliseconds
- Added setting `y-calculator` which allows you to choose the method of calculating the Y coordinate, information in config
- Added setting `minimal-radius` in profiles, which allows you to set the minimum radius for teleportation

Fixes:
- The falling animation now works correctly, teleports in center of block
- The empty animation now works correctly, as does the drop animation

## Animations, Fixes and another!

ATTENTION: The configuration has been changed!
Changes:
- Improved performance up to 170%
- Added ANIMATIONS:
  - Fall(The player falls from a certain height)
  - Others will be added later on request :)
- Added the `prtp.use` right for the main command and a line about "not enough rights" for it

And other minor changes)

## API added and fix

API added:
`xyz.distemi.prtp.api.PRTPAPI` 2 methods:
- `rtpPlayer(Player player, String execute)` - teleport player with custom options(for example second argument - default)
- `getAllProfiles()` - get all available profiles(loaded successfully)

Fixed teleport to void - if teleport location is void(don't have any place) then plugin search next location for teleport. For end world i recommend increase number of tries.

## Perfomance Improved, fixes and more...

Changes in version 1.1:
- Changes teleport sync to asynchronous (mega performance improvement).
- Added preventive blocks that cancel teleportation in some places (for example, water and lava).
- Fixed charging of players in creative and spectator gamemodes.
- and other changes...