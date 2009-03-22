/**
 * Services Toolbox
 * 
 * Allows for services to be register and unregister and to handle the complete service lifecycle.
 * 
 * Services play a special role in middleHeaven as all toolboxes are presented as services.
 * Services are simple interfaces whose implementations are registered with the {@code ServiceRegistry} 
 * 
 * After setup this services can be injected on any objects using the {@code WiringService}
 */
package org.middleheaven.core.services;