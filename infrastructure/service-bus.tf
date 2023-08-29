#HMC to hearings API
module "servicebus-subscription" {
  source                = "git@github.com:hmcts/terraform-module-servicebus-subscription?ref=master"
  name                  = "hmc-subs-to-cft-${var.env}"
  namespace_name        = "hmc-servicebus-${var.env}"
  topic_name            = "hmc-to-cft-${var.env}"
  resource_group_name   = "hmc-shared-${var.env}"
}
resource "azurerm_servicebus_subscription_rule" "topic_filter_rule_sscs" {
  name            = "hmc-servicebus-${var.env}-subscription-rule-bba3"
  subscription_id = module.servicebus-subscription.id
  filter_type     = "CorrelationFilter"
  correlation_filter {
    properties = {
      hmctsServiceId = "BHA1"
    }
  }
}
data "azurerm_key_vault" "hmc-key-vault" {
  name                = "hmc-${var.env}"
  resource_group_name = "hmc-shared-${var.env}"
}
data "azurerm_key_vault_secret" "hmc-servicebus-connection-string" {
  key_vault_id = "${data.azurerm_key_vault.hmc-key-vault.id}"
  name         = "hmc-servicebus-connection-string"
}

resource "azurerm_key_vault_secret" "hmc_to_et_hearings_api_servicebus-connection-string" {
  name         = "hmc-servicebus-connection-string"
  value        = data.azurerm_key_vault_secret.hmc-servicebus-connection-string.value
  key_vault_id = module.key-vault.key_vault_id
}
data "azurerm_key_vault_secret" "hmc-servicebus-shared-access-key" {
  key_vault_id = data.azurerm_key_vault.hmc-key-vault.id
  name         = "hmc-servicebus-shared-access-key"
}
resource "azurerm_key_vault_secret" "et-hmc-servicebus-shared-access-key-tf" {
  name         = "hmc-servicebus-shared-access-key-tf"
  value        = data.azurerm_key_vault_secret.hmc-servicebus-shared-access-key.value
  key_vault_id = module.key-vault.key_vault_id

  content_type = "secret"
  tags = merge(var.common_tags, {
    "source" : "Vault ${module.key-vault.key_vault_id}"
  })
}
