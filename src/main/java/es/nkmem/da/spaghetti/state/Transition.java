package es.nkmem.da.spaghetti.state;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Transition {
    private boolean resetPlayers = false;
    private String loadWorld = null;
    private String unloadWorld = null;
}
