module "servicebus-subscription" {
  source                = "git@github.com:hmcts/terraform-module-servicebus-subscription?ref=master"
  name                  = "hmc-subs-to-cft-${var.env}"
  namespace_name        = "hmc-servicebus-${var.env}"
  topic_name            = "hmc-to-cft-${var.env}"
  resource_group_name   = "hmc-shared-${var.env}"
}

data "azurerm_key_vault" "hmc-key-vault" {
  name                = "hmc-${var.env}"
  resource_group_name = "hmc-shared-${var.env}"
}

data "azurerm_key_vault_secret" "hmc-servicebus-connection-string" {
  key_vault_id = "${data.azurerm_key_vault.hmc-key-vault.id}"
  name         = "hmc-servicebus-connection-string"
}

resource "azurerm_key_vault_secret" "hmc_to_et_hearings_api_s2s_secret" {
  name         = "hmc-servicebus-connection-string"
  value        = data.azurerm_key_vault_secret.hmc-servicebus-connection-string.value
  key_vault_id = module.key-vault.key_vault_id
}
