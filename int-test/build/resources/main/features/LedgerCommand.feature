Feature: Simple ledger balance verifier

  Scenario Outline: for an account various entries to ledger gets included to the balance
    Given a call ledger entry endpoint with:
      | account     | <account>     |
      | amount      | <amount>      |
      | description | <description> |
      | type        | <type>        |
    Then account "<account>" balance query returns "<balance>"

    Examples:
      | account | amount | description  | type | balance |

      | 1       | 10.25  | First entry  | DR   | 10.25   |
      | 1       | 12.25  | Second entry | DR   | 22.50   |
      | 1       | 22.50  | Third entry  | CR   | 0.0     |