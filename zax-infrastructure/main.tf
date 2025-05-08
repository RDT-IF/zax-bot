resource "digitalocean_droplet" "zaxbot-1" {
  image  = "ubuntu-24-10-x64"
  name   = "ZaxBot-1"
  region = "sfo3"
  size   = "s-1vcpu-512mb-10gb"
  ssh_keys = [
    data.digitalocean_ssh_key.terraform.id
  ]

  connection {
    host    = self.ipv4_address
    user    = "root"
    type    = "ssh"
    private_key = file(var.pvt_key)
    timeout = "2m"
  }
}

resource "digitalocean_project" "zaxbot" {
  name = "ZaxBot"
  resources = [digitalocean_droplet.zaxbot-1.urn]
}
