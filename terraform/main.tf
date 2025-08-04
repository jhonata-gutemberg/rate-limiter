provider "aws" {
  region = "us-west-2"
}

module "s3-bucket" {
  source  = "terraform-aws-modules/s3-bucket/aws"
  version = "5.2.0"
}
