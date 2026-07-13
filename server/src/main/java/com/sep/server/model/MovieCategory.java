package com.sep.server.model;

public enum MovieCategory {
    ADVENTURE,ACTION,ANIMATION,DOKU,DRAMA,EROTIC,FAMILY,FANTASY,HORROR,COMEDY,CRIME,LOVE,MUSIC,SCIFI,OTHER,THRILLER,WESTERN;

    public String category(){
        String name=name().substring(0,1).toUpperCase();
        return name+name().substring(1).toLowerCase();
    }

}
