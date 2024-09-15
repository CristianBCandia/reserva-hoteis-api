provider "aws" {
  access_key = "test"
  secret_key = "test"
  region     = "us-east-1"
  skip_credentials_validation = true
  skip_requesting_account_id = true
  skip_metadata_api_check    = true
  endpoints {
    ec2 = "http://localhost:4566"
#     cloudwatch = "http://localhost:4566"
#     logs       = "http://localhost:4566"
  }
}

resource "aws_instance" "example" {
  ami           = "ami-0c55b159cbfafe1f0"
  instance_type = "t2.micro"

  tags = {
    Name = "example-instance"
  }
}

# resource "aws_cloudwatch_log_group" "example" {
#   name = "example-log-group"
#   tags = {}
# }
#
# resource "aws_cloudwatch_log_stream" "example" {
#   name           = "example-log-stream"
#   log_group_name = aws_cloudwatch_log_group.example.name
# }
#
# resource "aws_cloudwatch_metric_alarm" "example" {
#   alarm_name          = "example-alarm"
#   comparison_operator = "GreaterThanThreshold"
#   evaluation_periods  = "1"
#   metric_name         = "example-metric"
#   namespace           = "example-namespace"
#   period              = "60"
#   statistic           = "Sum"
#   threshold           = "1"
#
#   alarm_actions = []
# }