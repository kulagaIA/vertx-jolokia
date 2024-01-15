package com.simplesw.vertx.jolokia;

import io.vertx.core.logging.Logger;
import org.jolokia.server.core.service.api.LogHandler;

class JolokiaLogHandler implements LogHandler {
  private final Logger log;

  JolokiaLogHandler(Logger log) {
    this.log = log;
  }

  @Override
  public void debug(String message) {
    log.debug(message);
  }

  @Override
  public void info(String message) {
    log.info(message);
  }

  @Override
  public void error(String message, Throwable t) {
    log.error(message, t);
  }

  @Override
  public boolean isDebug() {
    return true;
  }
}