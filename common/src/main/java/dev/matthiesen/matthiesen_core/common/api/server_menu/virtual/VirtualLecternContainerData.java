package dev.matthiesen.matthiesen_core.common.api.server_menu.virtual;

import net.minecraft.world.inventory.ContainerData;

/**
 * Represents the data for a virtual lectern container, which is used to manage the state of a virtual lectern in a server menu.
 * This class implements the ContainerData interface to provide access to the current page of the lectern.
 */
public class VirtualLecternContainerData implements ContainerData {
    private int page = 0;

    /**
     * Returns the current page of the virtual lectern.
     *
     * @param id The index of the data to retrieve. Only index 0 is valid for the page.
     * @return The current page number if id is 0; otherwise, returns 0.
     */
    @Override
    public int get(int id) {
        return id == 0 ? this.page : 0;
    }

    /**
     * Sets the current page of the virtual lectern.
     *
     * @param id The index of the data to set. Only index 0 is valid for the page.
     * @param value The new page number to set if id is 0; otherwise, does nothing.
     */
    @Override
    public void set(int id, int value) {
        if (id == 0) this.page = value;
    }

    /**
     * Returns the number of data fields in this container data.
     *
     * @return The number of data fields, which is always 1 for the page.
     */
    @Override
    public int getCount() {
        return 1;
    }
}
