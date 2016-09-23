# Crispy-Waffle Passphrase Generator
Passphrase generator using Diceware-like lists to generate high-entropy passphrases.

# Running Passphrase Generator

The command to generate a passphrase is:

```
./crispywaffler --wordlist src/main/resources/com/github/ansell/crispywaffle/eff_large_wordlist.txt
```

There is also the option to override the default 6 word passphrase, with the minimum allowed being 4 words.

The command to generate a 7 word passphrase is:
```
./crispywaffler --wordlist src/main/resources/com/github/ansell/crispywaffle/eff_large_wordlist.txt --count 7
```

The bundled eff_large_wordlist.txt was created by the Electronic Frontier Foundation and was described at [https://www.eff.org/deeplinks/2016/07/new-wordlists-random-passphrases].

# Licence

Apache License, 2.0. See LICENSE file for more details
