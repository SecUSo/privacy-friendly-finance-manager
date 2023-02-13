# csv definition

charset = "utf8"
csv-fielddelimiter = ";"
enclose in -"- : only if necessary
empty lines or lines starting with -#- are treated as comments

shared as mime = text/csv with fixed name "Transactions-Export.csv"

Available columns

* date (without time as iso string)
* amount (using decimalseparator ".")
* note (transaction name or comment)
* category = name of selected category or empty
* account = name of selected account or empty