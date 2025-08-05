provider "aws" {
  access_key = "local"
  secret_key = "local"
  region     = "us-west-2"

  s3_use_path_style           = true
  skip_requesting_account_id  = true
  skip_credentials_validation = true
  skip_metadata_api_check     = true

  endpoints {
    s3  = "http://localhost:4566"
    sts = "http://localhost:4566"
  }
}

module "s3-bucket" {
  source  = "terraform-aws-modules/s3-bucket/aws"
  version = "5.2.0"

  bucket = "ratelimiter-configs"
}
