# [DEPRECATED] jsqry-cli

<span style="color:red; font-size:2em">⚠️ This project is superseded by [jsqry-cli2](https://github.com/jsqry/jsqry-cli2) ⚠️
</span>

Command-line jsqry (like jq)

```bash
$ echo '[{"a":1},{"a":2}]' | jsqry 'a' # query
[1, 2]

$ echo '[{"a":1},{"a":2}]' | jsqry -1 'a' # first
1
```

The output is pretty-printed by default.

## Install

Sorry, but only Linux x64 is supported at the moment. Hopefully this will improve.

To install or update the tool simply run the command below.

```bash
$ sudo bash -e -c "
curl -L https://github.com/jsqry/jsqry-cli/releases/download/v0.0.1/jsqry-linux-amd64.gz \
    | zcat > /usr/local/bin/jsqry
chmod +x /usr/local/bin/jsqry
echo \"jsqry \$(jsqry -v) installed successfully\" 
"
```