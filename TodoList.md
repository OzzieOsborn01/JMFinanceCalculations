Next Step List
The following list is an incomplete list of things to add or change
* Core
  * Implement Exceptions
  * Implement Taxes
  * Implement HOA Payment (Subscription)
  * Change Insurance to be flexible for term duration (not just annual ... every 6 months, monthly, weekly, etc)
* Processing
  * Add the following processors to mortgage processor
    * Taxes
    * Home Insurance
      * Instead of applying payment directly, move money in and out of escrow
    * HOA Payment Subscription
  * Update Mortgage Components with increases
  * Implement storing payments into holding/escrow account
  * Add Payment processing component to Mortgage Processor
    * Regular payment
    * Regular payment plus extra
    * Regular payment switch
    * Adhoc payments
    * Regular payment frequency changes
  * Add Interest Processor and Late fee processor
  * Simple Loan obligation processor
* Initialization
  * Upfront payment
  * Initial escrow
    * Home Insurance
    * Mortgage Insurance
  * Mortgage Insurance
    * Buydown
    * PMI (Conventional)
    * MIP (FHA) + upfront payment
* App