<body>
<h1>Readme</h1>
<p>Chit list - An app for recording transactions in a brew club.</p>

<div id="toc" class="toc">
<h2>Contents</h2>
<ol>
<li><a href="#app_overview">App overview</a></li>
<li><a href="#installation">Installation</a></li>
<li><a href="#setup">Setup</a></li>
<li><a href="#user_functions">User functions</a>
<ol>
<li><a href="#login_screen">Login screen</a></li>
<li><a href="#home_screen">Home screen</a></li>
<li><a href="#shop_screen">Shop screen</a></li>
<li><a href="#checkout_screen">Checkout screen</a></li>
<li><a href="#history_screen">History screen</a></li>
</ol>
</li>
<li><a href="#admin_functions">Admin functions</a>
<ol>
<li><a href="#settings_screen">Settings screen</a></li>
<li><a href="#accounts_screen">Accounts screen</a></li>
<li><a href="#items_screen">Items screen</a></li>
<li><a href="#transactions_screen">Transactions screen</a></li>
</ol>
</li>
</ol>
</div>

<h2><span id="app_overview">App overview</span></h2>
<p><span class="app_name">Chit list</span> is a self serve kiosk application for recording sums owed. Users log in under an account, choose items they want to purchase then checkout those items. The items purchased, the total price and the account used are recorded in a transaction. The balance of each account is then calculated by summing all transactions linked to each account. To pay off balances, administrators create transactions with a negative amount.</p>

<h2><span id="installation">Installation</span></h2>
<p>Failure to follow these instructions may result kiosk mode being disabled allowing users to close <span class="app_name">Chit list</span>, uninstall <span class="app_name">Chit list</span>, modify the database and more.</p>
<ol>
<li>Factory reset device</li>
<li>Do not add Google Account</li>
<li>Download and install .apk</li>
<li>Set device admin: <code>adb shell dpm set-device-owner gensokyo.hakurei.chitlist/.ChitlistDeviceAdminReveiver</code></li>
</ol>

<h2><span id="setup">Setup</span></h2>
<ol>
<li><a href="#login_screen">Login</a> using the default account.
Username: admin default, 
Password: <em>none</em></li>
<li>Go to <a href="#admin_functions">admin functions</a> from the top right dropdown menu</li>
<li>Add <a href="#accounts_screen">accounts</a></li>
<li>Add <a href="#items_screen">items</a> to the shop</li>
<li>Change the <a href="#accounts_screen">username</a> and <a href="#home_screen">password</a>. Leaving the default admin account is not recommended to maintain accountability. Each admin should have a seperate account.</li>
</ol>

<h2><span id="user_functions">User functions</span></h2>
<p>Users can <a href="#login_screen">login</a>, browse the <a href="#shop_screen">shop</a>, <a href="#checkout_screen">checkout</a> items and <a href="#history_screen">view</a> transactions recorded against their account.</p>
<h3><span id="login_screen">Login screen</span></h3>
<p>Login using a username and password.</p>
<p>The username field autocompletes when typing names and prepends a four digit <code>account_id</code>. The actual login credentials used are the <code>account_id</code> and the password. Thus, only the <code>account_id</code> is required for the username.</p>
<h3><span id="home_screen">Home screen</span></h3>
<p>Upon login users are taken to the home screen. Their full name is shown at the top of the screen as well as the account balance.</p>
<p>To logout, either use the back button or the top right dropdown menu. The menu is also where the password can be changed and where administrators can access <a href="#admin_functions">admin functions</a>.</p>
<h3><span id="shop_screen">Shop screen</span></h3>
<p>Click on items to add them to your cart. You can add items multiple times.</p>
<h3><span id="checkout_screen">Checkout screen</span></h3>
<p>Click on items to remove them from your cart. When satisfied, click the checkout button. This records the transactions and automatically logs you out.</p>
<h3><span id="history_screen">History screen</span></h3>
<p>A list of previous transactions. Your balance is the sum of all transactions listed. Transactions created by other users have a different color.</p>

<h2><span id="admin_functions">Admin functions</span></h2>
<p>Admins can perform app <a href="#settings_screen">maintenance</a> functions, create/update <a href="#accounts_screen">accounts</a>, create/update <a href="#items_screen">items</a> and create <a href="#transactions_screen">transactions</a>.</p>
<h3><span id="settings_screen">Settings screen</span></h3>
<p>Export transactions as comma separated values (csv), create a copy of the database, restore the database from a previous copy or exit the app.</p>
<h3><span id="accounts_screen">Accounts screen</span></h3>
<p>Click an account to edit it or the add button to create a new one.</p>
<p>Blue accounts have user access only. Teal accounts have administrator access. Grey accounts cannot log in.</p>
<p>The elements of an account are as follows:</p>
<ul>
<li>Key - A unique id used to refer to an account. Also referred to as <code>account_id</code>.</li>
<li>First name and last name - A common name to associate the account with. Can be used on the login screen to trigger autocompletion.</li>
<li>Location, contact number, email address - Optional
<li>Password hash - The password, cryptographically hashed using SHA-1. New accounts have no password which corresponds to a SHA-1 hash of <code>da39a3ee5e6b4b0d3255bfef95601890afd80709.</code></li>
<li>Administrator access - Enables/disables the account from accessing <a href="#admin_functions">admin functions</a>.</li>
<li>Interactive login - Enables/disables the account from <a href="#login_screen">logging in</a>. Note that the account still exists in the database and can have transactions recorded against it.</li>
</ul>
<h3><span id="items_screen">Items screen</span></h3>
<p>Click an item to edit it or the add button to create a new one.</p>
<p>Yellow items are enabled. Grey items are disabled.</p>
<p>The elements of an item are as follows:</p>
<ul>
<li>Key - A unique id used to refer to an item. Also referred to as <code>item_id</code>.</li>
<li>Name - The display name of the item.</li>
<li>Price - Used to calculate the <code>amount</code> of transactions during <a href="#checkout_screen">checkout</a>. Prices can be changed without affecting existing transactions. Price has no effect on transactions created through the <a href="#transactions_screen">admin interface</a>.</li>
<li>Image - The image used in the <a href="#shop_screen">shop</a>.</li>
<li>Enabled - Shows/hides the item from the <a href="#shop_screen">shop</a>.</li>
</ul>
<p>The automatically created items <code>Cash</code> and <code>EFT</code> are designed for use when reconciling balances.</p>
<h3><span id="transactions_screen">Transactions screen</span></h3>
<p>Click a transaction to edit it (comments only) or the add button to create a new one.</p>
<p>Red transactions have the same account_id and creator_id. Blue transactions have a differing account_id and creator_id. They can only be created by and administrator.</p>
<p>The elements of a transaction are as follows:</p>
<ul>
<li>Key - A unique id used to refer to a transaction. Also referred to as <code>transaction_id</code>.</li>
<li>Time - The time the transaction was created.</li>
<li>Account - The <a href="#accounts_screen">account</a> the transaction is recorded against. Similar to the login screen, only the four digit <code>account_id</code> is required and field autocompletes based on the <code>full name</code>.</li>
<li>Creator - The creator of the transaction. Cannot be modified. For shop transactions, this is the same as the account. Again, only the four digit <code>account_id</code> is required.</li>
<li>Item - The <a href="#items_screen">item</a> purchased or the item paid. Only the four digit <code>item_id</code> is required and the field autocompletes on the item <code>name</code>.</li>
<li>Amount - The amount used for calculating the balance. For <a href="#shop_screen">shop</a> transactions, this is the item <code>price</code> at the time of the transaction. For transactions created though the transactions screen, this can be arbitrarily set. Using a negative value will reduce balances and is used to pay off balances.</li>
<li>Comments - An optional field admins can use to annotate transactions.</li>
</ul>
</body>
</html>
