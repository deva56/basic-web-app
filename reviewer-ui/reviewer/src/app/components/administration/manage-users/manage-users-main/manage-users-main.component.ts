import { AfterViewInit, Component, ViewChild } from '@angular/core';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { UserDTO } from 'src/app/models/administration/userDTO';
import { AdministrationService } from 'src/app/services/administration/administration.service';

@Component({
  selector: 'app-manage-users-main',
  templateUrl: './manage-users-main.component.html',
  styleUrls: ['./manage-users-main.component.css']
})
export class ManageUsersMainComponent implements AfterViewInit {

  displayedColumns: string[] = ['position', 'username', 'email', 'isActivated', 'isDisabled', 'authorities', 'actions'];
  dataSource: MatTableDataSource<UserDTO> = new MatTableDataSource();
  @ViewChild(MatPaginator) paginator: MatPaginator | any;
  totalItemsCount: number = 0;
  pageIndex: number = 0;
  pageSize: number = 5;

  constructor(private administrationService: AdministrationService) { }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
    this.fetchUserData();
  }

  onPageChange(event: PageEvent): void {
    console.log("Ušli smo u on page change");

    this.totalItemsCount = event.length;
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
    this.fetchUserData();
  }

  fetchUserData() {
    const params = {
      page: this.pageIndex.toString(),
      pageSize: this.pageSize.toString()
    }

    this.administrationService.getAllUsers(params).subscribe(result => {
      const userDTOs: UserDTO[] = result.value.value;
      const userDTOSWithPositions: UserDTO[] = []
      userDTOs.forEach(element => {
        element.position = userDTOs.indexOf(element) + 1;
        userDTOSWithPositions.push(element);
      });
      this.dataSource = new MatTableDataSource(userDTOSWithPositions);
      this.paginator.length = result.value.totalItems;
    })
  }
}
