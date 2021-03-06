package com.jamesdpeters.entities;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "NATIONALITY", schema = "PUBLIC")
public class NationalityEntity {
    private Integer id;
    private String name;
    private List<PlayersEntity> players;
    private String nationality;
    private Integer regionId;
    private RegionsEntity region;
    private Map<Object, LeaguesEntity> leagues;

    @Id
    @Basic
    @Column(name = "ID")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "COUNTRY")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NationalityEntity that = (NationalityEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "nationality")
    public List<PlayersEntity> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayersEntity> players) {
        this.players = players;
    }

    @Basic
    @Column(name = "NATIONALITY")
    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    @Basic
    @Column(name = "REGION")
    public Integer getRegionId() {
        return regionId;
    }

    public void setRegionId(Integer regionId) {
        this.regionId = regionId;
    }

    @ManyToOne
    @JoinColumn(name = "REGION", referencedColumnName = "ID", insertable = false, updatable = false)
    public RegionsEntity getRegion() {
        return region;
    }

    public void setRegion(RegionsEntity region) {
        this.region = region;
    }

    @MapKey(name = "division")
    @OneToMany(mappedBy = "country", fetch = FetchType.EAGER)
    public Map<Object, LeaguesEntity> getLeagues() {
        return leagues;
    }

    public void setLeagues(Map<Object, LeaguesEntity> leagues) {
        this.leagues = leagues;
    }
}
