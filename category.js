const categories = {
  combat: { symbol: '⚔', title: 'Combat', intro: 'Sharpen timing, understand each exchange, and track your performance with focused combat tools.', features: [
    ['AutoClicker', 'Clicks while the attack button is held, with minimum and maximum CPS, randomized timing, and an optional target-only mode.'],
    ['WTap Trainer', 'Shows a live HUD cue for when to release and repress forward during PvP practice. It is a training display and never moves for you.'],
    ['HitSelect Trainer', 'Displays attack-cooldown timing and highlights the recommended moment to hit without automatically attacking.'],
    ['CPS Counter', 'Tracks left- and right-click activity over a rolling one-second window and displays both values on screen.'],
    ['Reach Display', 'Reports the distance to your current combat target so you can better understand spacing.'],
    ['Target Info', 'Shows the targeted entity’s name, current health, armor value, and distance in a compact overlay.']
  ]},
  movement: { symbol: '↗', title: 'Movement', intro: 'Make everyday movement more comfortable while keeping control firmly in your hands.', features: [
    ['Sprint', 'Keeps sprint enabled when you are moving forward and normal vanilla conditions allow it.'],
    ['ToggleSprint', 'Changes sprinting from hold-to-use into an easy toggle for longer movement sessions.'],
    ['ToggleSneak', 'Lets you toggle sneaking instead of continuously holding the sneak key.'],
    ['SafeWalk', 'Helps prevent accidental movement over exposed block edges while enabled.'],
    ['FreeLook', 'Temporarily rotates the camera independently with a configurable key and restores your normal direction when released.']
  ]},
  render: { symbol: '◉', title: 'Render', intro: 'Improve visual clarity using information already available to the vanilla client.', features: [
    ['Fullbright', 'Raises client-side scene brightness and safely restores your previous gamma value when disabled.'],
    ['ESP', 'Highlights selected loaded entity types, including players, mobs, and passive animals, with a configurable distance limit.'],
    ['Tracers', 'Draws directional lines toward selected visible, client-known entities.'],
    ['NameTags', 'Enhances name tags with optional health, distance, and armor information.'],
    ['ItemESP', 'Highlights dropped item entities that are already loaded by the client.'],
    ['StorageESP', 'Highlights loaded chests, trapped chests, barrels, shulker boxes, and ender chests.'],
    ['Projectiles', 'Displays an estimated trajectory for supported projectiles currently held by the player.'],
    ['HitColor', 'Provides a configurable color for an entity’s damage tint.'],
    ['BlockOverlay', 'Customizes the color and appearance of the targeted-block outline.'],
    ['ClearWater', 'Improves visibility while the camera is underwater.'],
    ['NoHurtCam', 'Reduces or disables hurt-camera movement using a configurable strength value.'],
    ['TimeChanger', 'Selects a client-side visual time: day, sunset, night, or a custom time.'],
    ['Weather', 'Overrides clear, rainy, or thunderstorm visuals on the client.']
  ]},
  world: { symbol: '⌁', title: 'World', intro: 'Navigate, remember, and inspect your loaded world without sending fake movement.', features: [
    ['FreeCam', 'Detaches the local camera while your player remains stationary, sends no fake movement packets, and returns cleanly when disabled.'],
    ['Waypoints', 'Stores named coordinates with a dimension, color, and distance display for reliable navigation.'],
    ['Breadcrumbs', 'Draws a temporary trail showing where you have traveled, with a configurable lifetime.'],
    ['Block Search', 'Highlights configured block types within the client’s currently loaded area.'],
    ['Storage Highlighting', 'Makes loaded storage blocks easier to locate without requesting unseen world data.']
  ]},
  player: { symbol: '▦', title: 'Player', intro: 'Reduce inventory friction with deliberate tools that act only under clear conditions.', features: [
    ['AutoTool', 'Selects the fastest suitable tool from your hotbar while you are actively breaking a block.'],
    ['AutoArmor', 'Equips a better armor piece from your inventory while the inventory screen is open.'],
    ['Inventory Manager', 'Offers sorting presets, hotbar preferences, and a configurable trash list. Items are never dropped without confirmation.'],
    ['Refill', 'Refills configured hotbar stacks from matching inventory stacks while the inventory screen is open.']
  ]},
  hud: { symbol: '⌘', title: 'HUD', intro: 'Build a heads-up display that shows exactly what matters and remembers your layout.', features: [
    ['Keystrokes', 'Displays WASD, LMB, RMB, Space, and CPS activity in a compact input panel.'],
    ['FPS', 'Shows the game’s current frames per second.'],
    ['Coordinates', 'Displays your current X, Y, and Z position.'],
    ['Armor Status', 'Keeps current armor protection visible at a glance.'],
    ['Potion Status', 'Shows active status effects without opening inventory.'],
    ['Clock', 'Displays a clean local clock.'],
    ['Compass', 'Shows your current facing direction and yaw.'],
    ['Ping', 'Displays current server latency when connected.'],
    ['Speed', 'Calculates and displays horizontal movement speed.'],
    ['Target Info', 'Provides a HUD version of name, health, armor, and distance information.'],
    ['Module List', 'Lists enabled modules alphabetically or by text width, with optional animation.'],
    ['Layout Editor', 'Drag every widget, then customize its scale, opacity, and color. Positions persist after restart.']
  ]}
};

const selected = categories[document.body.dataset.category] || categories.combat;
document.title = `${selected.title} features — Hog V1`;
document.querySelector('#category-symbol').textContent = selected.symbol;
document.querySelector('#category-name').textContent = selected.title;
document.querySelector('#category-intro').textContent = selected.intro;
document.querySelector('#feature-list').innerHTML = selected.features.map((feature, index) => `
  <article class="detail-card reveal" data-number="${String(index + 1).padStart(2, '0')}">
    <span class="tag">${selected.title.toUpperCase()}</span>
    <h2>${feature[0]}</h2>
    <p>${feature[1]}</p>
  </article>`).join('') + `
  <section class="category-cta reveal">
    <div><h2>Ready to try Hog V1?</h2><p>Minecraft 1.21.11 · Fabric Loader · Java 21</p></div>
    <a class="button primary" href="https://github.com/meehandaniel4/HogClient/releases/download/v1.0.0/hog-v1-1.0.0.jar">Download v1.0.0 <span>↓</span></a>
  </section>`;
