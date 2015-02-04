package me.rcmurphy.github.exception

class NotFoundException(uri: String) extends Exception(s"Not found: '$uri'") with ApiException {

}
