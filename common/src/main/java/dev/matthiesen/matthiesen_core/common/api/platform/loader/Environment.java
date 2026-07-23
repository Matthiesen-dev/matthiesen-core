package dev.matthiesen.matthiesen_core.common.api.platform.loader;

/**
 * An enumeration representing the different environments in which the mod can run. This is used to differentiate between
 * client and server code, and to ensure that certain code is only executed in the appropriate environment.
 */
public enum Environment {
    /**
     * Indicates that the current environment is a client environment. This can be used to differentiate between client
     * and server code, and to ensure that certain code is only executed on the client side.
     */
    CLIENT,

    /**
     * Indicates that the current environment is a server environment. This can be used to differentiate between client
     * and server code, and to ensure that certain code is only executed on the server side.
     */
    SERVER
}
