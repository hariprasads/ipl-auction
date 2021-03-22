import React from 'react';
import { Table} from 'react-bootstrap';
import { makeStyles } from '@material-ui/core/styles';
import FlightIcon from '@material-ui/icons/Flight';


const useStyles = makeStyles((theme) => ({
    yourTable: {
        marginTop: 10
    },
    overseasIcon: {
        fontSize: "small"
    },
    playersTeam: {
        fontSize: 16,
        fontWeight: 600,
        // marginBottom: 15
    }
}));

const PlayerTable = (props) => {

    const classes = useStyles();

    return(
        <>
            <span className={classes.playersTeam}>Purse Remaining - 80 crores</span>
            <Table striped bordered hover size="sm" className={classes.yourTable} >
                <thead>
                    <tr>
                    <th>#</th>
                    <th>Name</th>
                    <th>Role</th>
                    <th>Price</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                    <td>1</td>
                    <td>Dhoni</td>
                    <td>Keeper</td>
                    <td>5 crores</td>
                    </tr>
                    <tr>
                    <td>2</td>
                    <td>Stokes <FlightIcon className={classes.overseasIcon} /></td>
                    <td>AR</td>
                    <td></td>
                    </tr>
                    <tr>
                        <td>3</td>
                        <td>Bumrah</td>
                        <td>Bowl</td>
                        <td></td>
                    </tr>
                    <tr><td>4</td><td></td><td></td><td></td></tr>
                    <tr><td>5</td><td></td><td></td><td></td></tr>
                    <tr><td>6</td><td></td><td></td><td></td></tr>
                    <tr><td>7</td><td></td><td></td><td></td></tr>
                    <tr><td>8</td><td></td><td></td><td></td></tr>
                    <tr><td>9</td><td></td><td></td><td></td></tr>
                    <tr><td>10</td><td></td><td></td><td></td></tr>
                    <tr><td>11</td><td></td><td></td><td></td></tr>
                    <tr><td>12</td><td></td><td></td><td></td></tr>
                    <tr><td>13</td><td></td><td></td><td></td></tr>
                    <tr><td>14</td><td></td><td></td><td></td></tr>
                    <tr><td>15</td><td></td><td></td><td></td></tr>
                </tbody>
            </Table>
        </>
    )
}

export default PlayerTable;